package com.example.agrishop.farmer.data.models
data class Product(
    val id: String? = null,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val imageUrl: String = "",
    val farmName: String = ""

)