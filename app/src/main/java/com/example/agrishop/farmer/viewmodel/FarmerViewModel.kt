package com.example.agrishop.farmer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.agrishop.common.data.models.Notification
import com.example.agrishop.farmer.data.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class FarmerViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val farmerId = auth.currentUser?.uid

    init {
        checkAndLogLowStockNotifications()
    }

    private fun logLowStockNotification(productName: String, stock: Int) {
        val notificationsRef = farmerId?.let { database.child("notifications").child(it) }
        val message = "Low stock alert: $productName only $stock left in stock"

        // Check if stock is low enough to trigger a notification
        if (stock < 10) {
            // Check if a similar notification already exists
            notificationsRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (notificationSnapshot in snapshot.children) {
                        val notification = notificationSnapshot.getValue(Notification::class.java)
                        if (notification != null && notification.message.contains(productName)) {
                            // Update existing notification
                            val notificationId = notificationSnapshot.key
                            val updatedNotification = notificationId?.let {
                                Notification(
                                    it, message, System.currentTimeMillis()
                                )
                            }
                            notificationId?.let {
                                notificationsRef.child(notificationId).setValue(updatedNotification)
                            }
                            return
                        }
                    }

                    // Notification does not exist, add it
                    val notificationId = notificationsRef.push().key
                    val newNotification = notificationId?.let {
                        Notification(
                            it, message, System.currentTimeMillis()
                        )
                    }
                    notificationId?.let {
                        notificationsRef.child(notificationId).setValue(newNotification)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } else {
            // Stock is greater than 10, delete any existing notification
            notificationsRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (notificationSnapshot in snapshot.children) {
                        val notification = notificationSnapshot.getValue(Notification::class.java)
                        if (notification != null && notification.message.contains(productName)) {
                            // Delete existing notification
                            notificationSnapshot.ref.removeValue()
                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    // Function to check and log low stock notifications
    fun checkAndLogLowStockNotifications() {
        farmerId?.let { farmerId ->
            database.child("products").child(farmerId).get()
                .addOnSuccessListener { productsSnapshot ->
                    val products = mutableListOf<Product>()
                    val productNames = mutableListOf<String>()
                    for (productSnapshot in productsSnapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let {
                            products.add(it)
                            productNames.add(it.name)
                        }
                    }

                    products.forEach { product ->
                        if (product.stock < 10) {
                            logLowStockNotification(product.name, product.stock)
                        } else {
                            // Remove existing notifications if stock is >= 10
                            removeLowStockNotification(product.name)
                        }
                    }
                    // Clean up notifications for deleted products
                    cleanUpNotificationsForDeletedProducts(productNames)
                }.addOnFailureListener { exception ->

                }
        }
    }

    private fun removeLowStockNotification(productName: String) {
        val notificationsRef = farmerId?.let { database.child("notifications").child(it) }

        // Check if a notification exists for the product and remove it
        notificationsRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(Notification::class.java)
                    if (notification != null && notification.message.contains("Low stock alert: $productName")) {
                        notificationSnapshot.ref.removeValue()
                        return
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun cleanUpNotificationsForDeletedProducts(existingProductNames: List<String>) {
        val notificationsRef = farmerId?.let { database.child("notifications").child(it) }

        notificationsRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(Notification::class.java)
                    if (notification != null && notification.message.contains("Low stock alert:")) {
                        val productName = notification.message.substringAfter("Low stock alert: ")
                            .substringBefore(" only")
                        if (!existingProductNames.contains(productName)) {
                            notificationSnapshot.ref.removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getDashboardOverview() = liveData {
        val productsRef = farmerId?.let { database.child("products").child(it) }
        val notificationsRef = farmerId?.let { database.child("notifications").child(it) }

        val products = mutableListOf<Product>()
        val notifications = mutableListOf<Notification>()

        val productsSnapshot = productsRef?.get()?.await()
        if (productsSnapshot != null) {
            for (productSnapshot in productsSnapshot.children) {
                productSnapshot.getValue<Product>()?.let { products.add(it) }
            }
        }

        val notificationsSnapshot = notificationsRef?.get()?.await()
        if (notificationsSnapshot != null) {
            for (notificationSnapshot in notificationsSnapshot.children) {
                notificationSnapshot.getValue<Notification>()?.let { notifications.add(it) }
            }
        }
        val totalEarnings =
            notifications.filter { it.message.contains("Product bought by") }.sumOf {
                    it.message.substringAfter("for ").substringBefore(" rupees").toDoubleOrNull()
                        ?: 0.0
                }
        val numberOfOrders = notifications.count { it.message.contains("Product bought by") }
        val inventoryStatus = products

        val recentSales = notifications.filter { it.message.contains("Product bought by") }
            .sortedByDescending { it.timestamp }.take(5)
        val lowStockNotifications = notifications.filter { it.message.contains("Low stock alert") }
        emit(
            DashboardOverview(
                totalEarnings, numberOfOrders, inventoryStatus, recentSales, lowStockNotifications
            )
        )
    }

    data class DashboardOverview(
        val totalEarnings: Double,
        val numberOfOrders: Int,
        val inventoryStatus: List<Product>,
        val recentSales: List<Notification>,
        val lowStockNotifications: List<Notification>
    )
}