package com.example.agrishop.buyer.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.agrishop.other.ui.theme.md_theme_light_onBackground
import com.example.agrishop.other.ui.theme.md_theme_light_onErrorContainer

@Composable
fun FarmInfoCard(
    navController: NavHostController,
    farmName: String,
    address: String,
    farmUid: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Farm Name: $farmName", style = MaterialTheme.typography.bodyLarge,
                color = md_theme_light_onBackground

            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Text(
                    text = " Address: $address",
                    style = MaterialTheme.typography.bodyMedium,
                    color = md_theme_light_onBackground
                )
            }
            Text(
                text = "View Details",
                style = MaterialTheme.typography.bodySmall,
                color = md_theme_light_onErrorContainer,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        navController.navigate("farm_detail/$farmUid")
                    }

            )
        }
    }
}


