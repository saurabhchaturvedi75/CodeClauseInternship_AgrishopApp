package com.example.agrishop.buyer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrishop.farmer.data.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cartRef = database.getReference("carts").child(auth.currentUser?.uid ?: "")
    private val productsRef = database.getReference("products")

    private val uid = auth.currentUser?.uid

    private val _cartProducts = MutableLiveData<List<Product>?>()
    val cartProducts: MutableLiveData<List<Product>?> = _cartProducts

    init {
        fetchCartProducts()
    }

    private fun fetchCartProducts() {
         cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cartProductList = mutableListOf<Product>()
                val cartItemIds = dataSnapshot.children.mapNotNull { it.key }
                cartItemIds.forEach { productId ->
                    productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(productSnapshot: DataSnapshot) {
                            for (userSnapshot in productSnapshot.children) {
                                val product =
                                    userSnapshot.child(productId).getValue(Product::class.java)
                                product?.let { cartProductList.add(it) }
                            }
                            _cartProducts.value = cartProductList
                            Log.d("InventoryViewModel", "Cart products updated: $cartProductList")
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(
                                "InventoryViewModel",
                                "Failed to fetch product: ${databaseError.message}"
                            )
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("InventoryViewModel", "Failed to fetch cart items: ${databaseError.message}")
            }
        })
    }

    fun removeFromCart(product: Product) {
        product.id?.let { productId ->
            cartRef.child(productId).removeValue().addOnSuccessListener {
                // Update the local list immediately
                val updatedCartProducts = _cartProducts.value?.toMutableList()?.apply {
                    removeAll { it.id == productId }
                }
                _cartProducts.value = updatedCartProducts
            }.addOnFailureListener {
                Log.e("CartViewModel", "Failed to remove product from cart: ${it.message}")
            }
        }
    }
}
