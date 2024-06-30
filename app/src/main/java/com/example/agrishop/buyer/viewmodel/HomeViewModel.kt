package com.example.agrishop.buyer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrishop.farmer.data.models.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private val productsRef = database.getReference("products")

    private val _farms =
        MutableLiveData<List<Triple<String, String, String>>>() // List of triples (UID, Farm Name, Address)
    val farms: LiveData<List<Triple<String, String, String>>> get() = _farms

    private val _farmProducts = MutableLiveData<List<Product>>()
    val farmProducts: LiveData<List<Product>> get() = _farmProducts

    init {
        fetchAllFarms()
    }


    private fun fetchAllFarms() {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val farmsList = mutableListOf<Triple<String, String, String>>()
                for (snapshot in dataSnapshot.children) {
                    val userType = snapshot.child("userType").getValue(String::class.java)
                    if (userType == "Farmer") {

                        val farmUid = snapshot.key ?: ""
                        val farmName =
                            snapshot.child("farmName").getValue(String::class.java) ?: "N/A"
                        val address =
                            snapshot.child("farmAddress").getValue(String::class.java) ?: "N/A"
                        farmsList.add(Triple(farmUid, farmName, address))
                    }
                }
                _farms.value = farmsList
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun fetchProductsForFarm(farmName: String) {
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (userSnapshot in dataSnapshot.children) {
                    for (productSnapshot in userSnapshot.children) {
                        val fetchedProduct = productSnapshot.getValue(Product::class.java)
                        fetchedProduct?.let {
                            if (it.farmName == farmName) {
                                productList.add(it)
                            }
                        }
                    }
                }
                _farmProducts.value = productList
                Log.d(
                    "InventoryViewModel",
                    "Fetched ${productList.size} products for farm: $farmName"
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("InventoryViewModel", "Error fetching products: ${databaseError.message}")
                println("Failed to fetch products: ${databaseError.message}")
            }
        })
    }
}

