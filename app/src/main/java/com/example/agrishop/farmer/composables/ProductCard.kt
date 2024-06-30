package com.example.agrishop.farmer.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.agrishop.farmer.data.models.Product
import com.example.agrishop.farmer.viewmodel.AddProductViewModel
import com.example.agrishop.other.ui.theme.md_theme_dark_onSecondaryContainer2
import com.example.agrishop.other.ui.theme.md_theme_dark_primary
import com.example.agrishop.other.ui.theme.md_theme_light_onSecondaryContainer
import kotlinx.coroutines.launch

@Composable
fun ProductCard(product: Product) {
    val coroutineScope = rememberCoroutineScope()
    var showEditDialog by remember { mutableStateOf(false) }
    val addProductViewModel: AddProductViewModel = viewModel()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(md_theme_dark_onSecondaryContainer2)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = md_theme_dark_primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Price: ",
                            color = md_theme_light_onSecondaryContainer
                        )
                        Text(
                            text = " ${product.price} Rs/kg",
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Stock: ",
                            color = md_theme_light_onSecondaryContainer
                        )
                        Text(
                            text = "${product.stock} kg",

                            )
                    }

                }
                product.imageUrl?.let {
                    Card(
                        modifier = Modifier.size(100.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .weight(1f)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Description: ${product.description}",
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { showEditDialog = true }) {
                    Text("Edit")
                }
                Button(onClick = { showDeleteDialog = true }) {
                    Text("Delete")
                }
            }

        }
    }
    if (showEditDialog) {
        EditProductDialog(
            product = product,
            onDismiss = { showEditDialog = false },
            onSave = { updatedProduct ->
                coroutineScope.launch {
                    addProductViewModel.updateProduct(updatedProduct)
                    showEditDialog = false

                }
            }
        )
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Product") },
            text = { Text("Are you sure you want to delete this product?") },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        addProductViewModel.deleteProduct(product)
                        showDeleteDialog = false
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
