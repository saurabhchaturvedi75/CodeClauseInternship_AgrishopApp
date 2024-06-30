package com.example.agrishop.buyer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.buyer.composables.InventoryItemCard
import com.example.agrishop.buyer.composables.SearchBar
import com.example.agrishop.buyer.viewmodel.InventoryViewModel

@Composable
fun Inventory(navHostController: NavHostController) {
    val inventoryViewModel: InventoryViewModel = viewModel()
    val products by inventoryViewModel.products.observeAsState(emptyList())
    val cartProducts by inventoryViewModel.cartProducts.observeAsState(emptySet())
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        inventoryViewModel.fetchProducts()
        inventoryViewModel.fetchCartProducts()
    }

    val filteredProducts = if (searchQuery.isEmpty()) {
        products
    } else {
        products.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = { query ->
                searchQuery = query
            }
        )

        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No products available")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxHeight()
            ) {
                items(filteredProducts) { product ->
                    val isInCart = cartProducts.contains(product.id)
                    InventoryItemCard(
                        product = product,
                        isInCart = isInCart,
                        onAddToCartClick = {
                            if (isInCart) {
                                inventoryViewModel.removeFromCart(product)
                            } else {
                                inventoryViewModel.addToCart(product)
                            }
                        }
                    )
                }
            }
        }
    }
}
