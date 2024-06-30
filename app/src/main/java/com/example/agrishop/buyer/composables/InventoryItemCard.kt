package com.example.agrishop.buyer.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.agrishop.farmer.data.models.Product
import com.example.agrishop.other.ui.theme.md_theme_dark_onSecondaryContainer2
import com.example.agrishop.other.ui.theme.md_theme_dark_primary
import com.example.agrishop.other.ui.theme.md_theme_light_onSecondaryContainer

@Composable
fun InventoryItemCard(
    product: Product,
    isInCart: Boolean,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {


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
                            text = "Price: ", color = md_theme_light_onSecondaryContainer
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
                            text = "Stock: ", color = md_theme_light_onSecondaryContainer
                        )
                        Text(
                            text = "${product.stock} kg",
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Farm Name: ${product.farmName}",
                    )

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

            Row {


                Text(
                    text = "Description: ${product.description}",
                    modifier = Modifier.weight(0.7f)
                )

                Icon(
                    imageVector = if (isInCart) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                    contentDescription = "Add to Cart",
                    modifier = Modifier
                        .size(24.dp)
                        .weight(0.3f)
                        .clickable { onAddToCartClick() }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


    }
}
