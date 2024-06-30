package com.example.agrishop.buyer.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.agrishop.buyer.screens.Cart
import com.example.agrishop.buyer.screens.FarmDetailScreen
import com.example.agrishop.buyer.screens.HomeScreen
import com.example.agrishop.buyer.screens.Inventory
import com.example.agrishop.buyer.screens.NotificationBuyerScreen
import com.example.agrishop.buyer.screens.ProfileScreen
import com.example.agrishop.common.data.models.BottomNavItem
import com.example.agrishop.common.navigation.Routes
import com.example.agrishop.common.viewmodel.FarmDetailViewModel

@Composable
fun BottomNav(navController: NavHostController) {

    val navController1 = rememberNavController()

    Scaffold(bottomBar = { MyBottomBar(navController1) }) { padding ->
        NavHost(
            navController = navController1, startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(navController1)
            }
            composable(Routes.Inventory.route) {
                Inventory(navController)
            }
            composable(Routes.Cart.route) {
                Cart(navController)
            }
            composable(Routes.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Routes.NotificationBuyer.route) {
                NotificationBuyerScreen(navController)
            }
            composable("farm_detail/{farmUid}") { backStackEntry ->
                val farmUid = backStackEntry.arguments?.getString("farmUid") ?: ""
                val farmDetailViewModel: FarmDetailViewModel = viewModel()
                farmDetailViewModel.fetchFarmDetails(farmUid)

                val farmDetails by farmDetailViewModel.farmDetails.observeAsState()
                farmDetails?.let {
                    FarmDetailScreen(farm = it)
                }
            }

        }

    }
}


@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "Home",
            Routes.Home.route,
            Icons.Default.Home,
            modifier = Modifier
                .size(20.dp)

        ),

        BottomNavItem(
            "Store",
            Routes.Inventory.route,
            Icons.AutoMirrored.Filled.List,
            modifier = Modifier
                .size(20.dp)


        ),
        BottomNavItem(
            "Cart",
            Routes.Cart.route,
            Icons.Default.ShoppingCart,
            modifier = Modifier
                .size(20.dp)


        ),
        BottomNavItem(
            "History",
            Routes.NotificationBuyer.route,
            Icons.Default.Notifications,
            modifier = Modifier
                .size(20.dp)


        ),
        BottomNavItem(
            "Profile",
            Routes.Profile.route,
            Icons.Default.Person,
            modifier = Modifier
                .size(20.dp)


        )

    )

    BottomAppBar {
        list.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route

            NavigationBarItem(
                alwaysShowLabel = true,
                label = { Text(it.title) },
                selected = selected, onClick = {
                    navController1.navigate(it.route) {
                        popUpTo(navController1.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title,
                        modifier = it.modifier
                    )
                }
            )
        }
    }
}