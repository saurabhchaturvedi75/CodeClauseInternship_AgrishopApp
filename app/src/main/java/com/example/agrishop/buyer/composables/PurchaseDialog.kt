package com.example.agrishop.buyer.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agrishop.buyer.viewmodel.BuyerViewModel
import com.example.agrishop.farmer.data.models.Product

@Composable
fun PurchaseDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val buyerViewModel: BuyerViewModel = viewModel()
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }
    var errorMessage by remember { mutableStateOf("") }
    val totalCost = product.price * quantity

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Purchase ${product.name}")
        },
        text = {
            Column {
                Text(text = "Price: ${product.price} Rs/kg")
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Quantity (kg): ")
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = quantity.toString(),
                        onValueChange = { value ->
                            val newQuantity = value.toIntOrNull() ?: 1
                            if (newQuantity > product.stock) {
                                errorMessage = "Quantity exceeds available stock"
                            } else {
                                errorMessage = ""
                                quantity = newQuantity
                            }
                        },
                        modifier = Modifier.width(60.dp)
                    )
                }
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Total Cost: $totalCost Rs")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (quantity <= product.stock) {
                        buyerViewModel.buyProduct(
                            context,
                            product.farmName,
                            product.name,
                            quantity,
                            totalCost
                        )
                        onConfirm(quantity)
                    }
                },
                enabled = quantity <= product.stock
            ) {
                Text("Confirm Buy")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
