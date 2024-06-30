package com.example.agrishop.buyer.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrishop.farmer.data.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InventoryViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val cartRef = database.getReference("carts").child(auth.currentUser?.uid ?: "")

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _cartProducts = MutableLiveData<Set<String>>()
    val cartProducts: LiveData<Set<String>> = _cartProducts

    private val productsRef = database.getReference("products")

    fun fetchProducts() {
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (userSnapshot in dataSnapshot.children) {
                    for (productSnapshot in userSnapshot.children) {
                        val fetchedProduct = productSnapshot.getValue(Product::class.java)
                        fetchedProduct?.let { productList.add(it) }
                    }
                }
                _products.value = productList
                Log.d("InventoryViewModel", "Fetched ${productList.size} products")
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.d("InventoryViewModel", "Fetched error in products")

                println("Failed to fetch products: ${databaseError.message}")
            }
        })
    }


    fun fetchCartProducts() {
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cartProductSet = mutableSetOf<String>()
                for (cartSnapshot in dataSnapshot.children) {
                    val productId = cartSnapshot.key
                    productId?.let { cartProductSet.add(it) }
                }
                _cartProducts.value = cartProductSet
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "InventoryViewModel",
                    "Failed to fetch cart products: ${databaseError.message}"
                )
            }
        })
    }

    fun addToCart(product: Product) {
        product.id?.let { cartRef.child(it).setValue(true) }
    }

    fun removeFromCart(product: Product) {
        product.id?.let { cartRef.child(it).removeValue() }
    }
}

