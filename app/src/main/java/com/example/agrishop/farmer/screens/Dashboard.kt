package com.example.agrishop.farmer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.common.navigation.Routes
import com.example.agrishop.farmer.data.models.Product
import com.example.agrishop.farmer.viewmodel.FarmerViewModel

@Composable
fun Dashboard(navController: NavHostController) {

    val viewModel: FarmerViewModel = viewModel()
    val dashboardOverview = viewModel.getDashboardOverview().observeAsState()

    LaunchedEffect(Unit) {
        viewModel.checkAndLogLowStockNotifications()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()

            .padding(horizontal = 16.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Farmer Dashboard",

                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(5.dp))



            dashboardOverview.value?.let { overview ->
                DashboardSummary(
                    totalEarnings = overview.totalEarnings,
                    numberOfOrders = overview.numberOfOrders
                )
                Spacer(modifier = Modifier.height(5.dp))

                DashboardSectionTitle(title = "Inventory Status", navController)
                Spacer(modifier = Modifier.height(1.dp))

                LazyRow(

                ) {
                    items(overview.inventoryStatus) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(7.dp),
                            elevation = CardDefaults.cardElevation(1.dp),
                            colors = CardDefaults.cardColors(Color(0xFFE3F2FD))
                        ) {
                            InventoryStatusItem(product)

                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))

                DashboardSectionTitle(
                    title = "Recent Sales",
                    navController
                )
                Spacer(modifier = Modifier.height(1.dp))

                LazyColumn(
                    modifier = Modifier.height(150.dp)
                ) {
                    items(overview.recentSales) { notification ->
                        Card(
                            modifier = Modifier.padding(6.dp),
                            colors = CardDefaults.cardColors(Color(0xFFFDF5CC))

                        ) {
                            NotificationItem(notification.message)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                DashboardSectionTitle(title = "Low Stock Alerts", navController)
                Spacer(modifier = Modifier.height(1.dp))
                LazyColumn {
                    items(overview.lowStockNotifications) { notification ->
                        Card(
                            modifier = Modifier.padding(6.dp),
                            colors = CardDefaults.cardColors(Color(0xFFCAFDCC))

                        ) {
                            NotificationItem(notification.message)

                        }
                    }
                }
            }
        }
    }

}


@Composable
fun DashboardSummary(totalEarnings: Double, numberOfOrders: Int) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .padding(13.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(Color(0xFFF8DFE7)),

        ) {
        Column(
            modifier = Modifier
                .padding(9.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            Text(
                text = "Total Earnings : $totalEarnings Rs",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Number of Orders : $numberOfOrders",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun DashboardSectionTitle(title: String, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "View All",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    if (title == "Inventory Status")
                        navController.navigate(Routes.Product.route)
                    else navController.navigate(Routes.Notification.route)


                }
        )

    }
}

@Composable
fun InventoryStatusItem(product: Product) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp)
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = " : ${product.stock} kg",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NotificationItem(message: String) {

    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(9.dp)

    )

}
