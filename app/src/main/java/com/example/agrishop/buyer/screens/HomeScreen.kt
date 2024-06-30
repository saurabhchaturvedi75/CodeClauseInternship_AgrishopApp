package com.example.agrishop.buyer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.buyer.composables.AutoScrollingPager
import com.example.agrishop.buyer.composables.FarmInfoCard
import com.example.agrishop.buyer.composables.HomeProductCard
import com.example.agrishop.buyer.viewmodel.HomeViewModel
import com.example.agrishop.buyer.viewmodel.InventoryViewModel
import com.example.agrishop.common.navigation.Routes
import com.example.agrishop.common.viewmodel.CommonViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val commonViewModel: CommonViewModel = viewModel()
    val inventoryViewModel: InventoryViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val products by inventoryViewModel.products.observeAsState(emptyList())
    var username by remember { mutableStateOf<String?>(null) }
    val latestProducts = products.takeLast(10)
    val farms by homeViewModel.farms.observeAsState(emptyList())

    val pagerState = rememberPagerState()

    LaunchedEffect(Unit) {
        if (uid != null) {
            commonViewModel.getUsernameByUid(uid) {
                username = it.toString()
            }
        }
        inventoryViewModel.fetchProducts()
        inventoryViewModel.fetchCartProducts()
    }
    AutoScrollingPager(pagerState)

    val colors = listOf(
        Color(0xFFE3F2FD), // Light Blue
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFE1BEE7), // Light Purple
        Color(0xFFC8E6C9), // Light Green
        Color(0xFFFFCDD2), // Light Red
        Color(0xFFD1C4E9), // Light Purple
        Color(0xFFBBDEFB), // Light Blue
        Color(0xFFFFF176), // Light Yellow
        Color(0xFFE6EE9C), // Light Green
        Color(0xFFFF8A65)  // Light Orange
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Welcome, $username!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Products", style = MaterialTheme.typography.bodyLarge)
            Text(

                "View All Products",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Inventory.route)

                }
            )
        }

        HorizontalPager(
            count = latestProducts.size,
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            HomeProductCard(latestProducts[page], navController, colors[page % colors.size])
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text("Farms near you", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(3.dp))

        FarmList(navController = navController, farms = farms)

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun FarmList(navController: NavHostController, farms: List<Triple<String, String, String>>) {
    LazyRow {
        items(farms) { farm ->
            FarmInfoCard(navController, farm.second, farm.third, farm.first)
        }
    }
}

