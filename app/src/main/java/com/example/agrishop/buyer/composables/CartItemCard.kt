package com.example.agrishop.buyer.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.agrishop.buyer.viewmodel.CartViewModel
import com.example.agrishop.farmer.data.models.Product
import com.example.agrishop.other.ui.theme.md_theme_light_onSecondaryContainer
import com.example.agrishop.other.ui.theme.md_theme_light_tertiary

@Composable
fun CartItemCard(product: Product, onBuyClick: () -> Unit) {

    val cartViewModel: CartViewModel = viewModel()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "Price: ${product.price} Rs/kg")
                    Text(text = "Stock: ${product.stock} kg")
                }
                product.imageUrl.let {
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { cartViewModel.removeFromCart(product) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_tertiary,
                    )
                ) {
                    Text("Remove")
                }

                Button(
                    onClick = onBuyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_onSecondaryContainer,
                    )
                ) {
                    Text("Buy")
                }

            }

        }
    }
}
