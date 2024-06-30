package com.example.agrishop.farmer.viewmodel


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.common.data.models.Notification
import com.example.agrishop.farmer.data.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.UUID


class AddProductViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val productsRef = database.getReference("products")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val uid = auth.currentUser?.uid
    private val notificationsRef = database.getReference("notifications")

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>?> = _products

    fun addProduct(
        context: Context,
        name: String,
        description: String,
        price: Double,
        stock: Int,
        imageUri: Uri,
        farmName: String
    ) {
        val productId = productsRef.push().key
        val imageRef = storageRef.child("product_images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    if (productId != null) {
                        val product = Product(
                            productId,
                            name,
                            description,
                            price,
                            stock,
                            imageUrl,
                            farmName
                        )
                        productsRef.child(uid!!).child(productId).setValue(product)
                            .addOnSuccessListener {

                                // Update the products list immediately
                                fetchProducts()
                                logNotification("Product added", name)
                            }

                    }
                }
            }
            .addOnFailureListener { exception ->

                Toast.makeText(
                    context,
                    "Failed to upload image: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun logNotification(action: String, productName: String) {
        val notificationId = notificationsRef.push().key

        val message = buildAnnotatedString {
            append("$action: ")
            pushStyle(SpanStyle(color = Color.Red))
            append(productName)
            pop()
        }
        val notification = notificationId?.let {
            Notification(
                it,
                message.toString(),
                System.currentTimeMillis()
            )
        }
        if (notificationId != null) {
            notificationsRef.child(uid!!).child(notificationId).setValue(notification)
        }
    }

    fun fetchProducts() {
        productsRef.child(uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let { productList.add(it) }

                }
                _products.value = productList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to fetch products: ${databaseError.message}")
            }
        })
    }

    fun updateProduct(product: Product) {
        val productId = product.id
        if (productId != null) {
            productsRef.child(uid!!).child(productId).setValue(product)
                .addOnSuccessListener {
                    logNotification("Product updated", product.name)
                }
        }
    }

    //
    fun deleteProduct(product: Product) {
        val productId = product.id
        if (productId != null) {
            productsRef.child(uid!!).child(productId).removeValue()
                .addOnSuccessListener {
                    logNotification("Product deleted", product.name)
                }
        }
    }

    fun getFarmName(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                database.reference.child("users").child(userId).child("farmName")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val farmName = snapshot.getValue(String::class.java)
                            onResult(farmName)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onResult(null)
                        }
                    })
            } else {
                onResult(null)
            }
        }
    }
}
