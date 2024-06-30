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
import com.example.agrishop.buyer.composables.CartItemCard
import com.example.agrishop.buyer.composables.PurchaseDialog
import com.example.agrishop.buyer.composables.SearchBar
import com.example.agrishop.buyer.viewmodel.CartViewModel
import com.example.agrishop.farmer.data.models.Product


@Composable
fun Cart(navHostController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel()
    val cartItems by cartViewModel.cartProducts.observeAsState(emptyList())
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredCartItems = if (searchQuery.isEmpty()) {
        cartItems
    } else {
        cartItems?.filter {
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

        if (filteredCartItems != null) {
            if (filteredCartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No items in cart")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(filteredCartItems.orEmpty()) { cartItem ->
                        CartItemCard(cartItem) {
                            selectedProduct = cartItem
                            showDialog = true
                        }
                    }
                }
            }
        }
    }


    selectedProduct?.let { product ->
        if (showDialog) {
            PurchaseDialog(
                product = product,
                onDismiss = { showDialog = false },
                onConfirm = { quantity ->

                    cartViewModel.removeFromCart(product)
                    showDialog = false
                }
            )
        }
    }
}
