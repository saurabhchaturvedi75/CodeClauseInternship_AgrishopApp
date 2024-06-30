package com.example.agrishop.buyer.data.models

import com.example.agrishop.farmer.data.models.Product

data class Farm(
    val farmUid: String = "",
    val farmName: String = "",
    val farmAddress: String = "",
    val farmDescription: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val businessRegNumber: String = "",
    val products: List<Product> = emptyList()

)
