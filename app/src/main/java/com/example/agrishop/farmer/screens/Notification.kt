package com.example.agrishop.farmer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.common.data.models.Notification
import com.example.agrishop.common.viewmodel.NotificationViewModel
import com.example.agrishop.farmer.viewmodel.FarmerViewModel
import com.example.agrishop.other.ui.theme.md_theme_light_primary
import com.example.agrishop.other.ui.theme.md_theme_light_secondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationScreen(navController: NavHostController) {


    val notificationViewModel: NotificationViewModel = viewModel()
    val notifications by notificationViewModel.notifications.observeAsState(emptyList())
    val farmerViewModel: FarmerViewModel = viewModel()

    LaunchedEffect(Unit) {
        farmerViewModel.checkAndLogLowStockNotifications()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Notifications", style = typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(notifications.reversed()) { notification ->
                NotificationCard(notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
//
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val messageParts = notification.message.split(": ")
            val actionPart = messageParts.getOrNull(0) ?: ""
            val productNamePart = messageParts.getOrNull(1) ?: ""

            val actionColor = when {
                actionPart.contains("added", ignoreCase = true) -> md_theme_light_secondary
                actionPart.contains("updated", ignoreCase = true) -> Color.Blue
                actionPart.contains("bought", ignoreCase = true) -> md_theme_light_primary
                actionPart.contains("paid", ignoreCase = true) -> md_theme_light_primary
                actionPart.contains("deleted", ignoreCase = true) -> Color.Red
                actionPart.contains("Low stock alert", ignoreCase = true) -> Color.Magenta
                else -> Color.Black
            }
            val styledMessage = buildAnnotatedString {
                withStyle(style = SpanStyle(color = actionColor)) {
                    append(actionPart)
                }
                append(": ")
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(productNamePart)
                }
            }


            Text(styledMessage, style = typography.bodyLarge)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(
                    Date(
                        notification.timestamp
                    )
                ),
                style = typography.bodySmall
            )
        }
    }

}