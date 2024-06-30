package com.example.agrishop.buyer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.common.viewmodel.NotificationViewModel

@Composable
fun NotificationBuyerScreen(navController: NavHostController) {


    val notificationViewModel: NotificationViewModel = viewModel()
    val notifications by notificationViewModel.notifications.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("History", style = typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(notifications.reversed()) { notification ->
                com.example.agrishop.farmer.screens.NotificationCard(notification)
            }
        }
    }
}
