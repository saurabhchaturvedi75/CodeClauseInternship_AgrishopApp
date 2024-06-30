package com.example.agrishop.buyer.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrishop.buyer.data.models.Buyer
import com.example.agrishop.common.data.models.Notification
import com.example.agrishop.common.viewmodel.CommonViewModel
import com.example.agrishop.farmer.data.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuyerViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private val notificationsRef = database.getReference("notifications")
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val uid = auth.currentUser?.uid
    private val commonViewModel: CommonViewModel = CommonViewModel()

    val productsRef = database.getReference("products")

    val userData = firebaseLiveData()

    private fun firebaseLiveData(): MutableLiveData<Buyer> {
        val liveData = MutableLiveData<Buyer>()

        uid?.let { uid ->
            usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Buyer::class.java)
                    user?.let {
                        liveData.postValue(it)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        return liveData
    }

    private fun logNotification(farmerUid: String, message: String) {
        val notificationId = notificationsRef.push().key
        val notification = notificationId?.let {
            Notification(
                it, message, System.currentTimeMillis()
            )
        }
        if (notificationId != null) {
            notificationsRef.child(farmerUid).child(notificationId).setValue(notification)
        }
    }

    fun buyProduct(
        context: Context,
        farmName: String,
        productName: String,
        quantity: Int,
        price: Double
    ) {
        getFarmerUidByFarmName(farmName) { farmerUid ->
            if (farmerUid != null) {
                if (uid != null) {
                    commonViewModel.getUsernameByUid(uid) { buyerUsername ->
                        if (buyerUsername != null) {
                            logNotification(
                                farmerUid,
                                "Product bought by $buyerUsername: $productName ($quantity kg) for $price rupees"
                            )
                            logNotification(
                                uid,
                                "You paid $price rupees for $quantity kg of : $productName"
                            )
                            updateProductStock(farmName, productName, quantity)

                        } else {
                            Toast.makeText(context, "Buyer username not found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Farm not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFarmerUidByFarmName(farmName: String, callback: (String?) -> Unit) {
        usersRef.orderByChild("farmName").equalTo(farmName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val uid = snapshot.key
                        callback(uid)
                        return
                    }
                    callback(null)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }

    fun updateProductStock(farmName: String, productName: String, quantity: Int) {


        getFarmerUidByFarmName(farmName) { farmerUid ->
            if (farmerUid != null) {
                val farmerProductsRef = database.getReference("products").child(farmerUid)
                // Listen for a single value event to find the product by its name
                farmerProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (productSnapshot in snapshot.children) {
                            val product = productSnapshot.getValue(Product::class.java)
                            if (product?.name == productName) {
                                val newStock = product.stock - quantity
                                if (newStock >= 0) {
                                    // Update the product's stock
                                    product.id?.let {
                                        farmerProductsRef.child(it).child("stock")
                                            .setValue(newStock)
                                    }
                                } else {
                                }
                                break
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            } else {
            }
        }
    }


}


