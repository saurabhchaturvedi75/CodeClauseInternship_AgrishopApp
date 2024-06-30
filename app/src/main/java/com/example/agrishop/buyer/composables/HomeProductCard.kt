package com.example.agrishop.buyer.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.agrishop.farmer.data.models.Product

@Composable
fun HomeProductCard(product: Product, navController: NavHostController, cardColor: Color) {
    Card(
        modifier = Modifier
             .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),

        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            product.imageUrl.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(140.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                             .fillMaxSize()

                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    product.name, style = MaterialTheme.typography.headlineSmall,

                    modifier = Modifier.weight(0.6f)
                )
                Text(
                    "${product.price} Rs/Kg.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.4f),
                    textAlign = TextAlign.End
                )
            }
        }


    }
}
 