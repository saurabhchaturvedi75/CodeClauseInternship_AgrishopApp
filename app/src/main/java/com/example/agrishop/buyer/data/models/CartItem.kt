package com.example.agrishop.buyer.data.models

import com.example.agrishop.farmer.data.models.Product

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)