package com.example.agrishop.farmer.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.agrishop.common.data.models.BottomNavItem
import com.example.agrishop.common.navigation.Routes
import com.example.agrishop.farmer.screens.Dashboard
import com.example.agrishop.farmer.screens.FarmerProfile
import com.example.agrishop.farmer.screens.NotificationScreen
import com.example.agrishop.farmer.screens.Product

@Composable
fun BottomNav2(navController: NavHostController) {

    val navController1 = rememberNavController()

    Scaffold(bottomBar = { MyBottomBar2(navController1) }) { padding ->
        NavHost(
            navController = navController1, startDestination = Routes.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Dashboard.route) {
                Dashboard(navController1)
            }
            composable(Routes.Product.route) {
                Product(navController)
            }
            composable(Routes.Notification.route) {
                NotificationScreen(navController)
            }
            composable(Routes.FarmerProfile.route) {
                FarmerProfile(navController)
            }

        }

    }
}


@Composable
fun MyBottomBar2(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "Dashboard",
            Routes.Dashboard.route,
            Icons.Default.Home,
            modifier = Modifier
                .size(20.dp)

        ),
        BottomNavItem(
            "Product",
            Routes.Product.route,
            Icons.Default.ShoppingCart,
            modifier = Modifier
                .size(20.dp)


        ),


        BottomNavItem(
            "Notification",
            Routes.Notification.route,
            Icons.Default.Notifications,
            modifier = Modifier
                .size(20.dp)


        ),

        BottomNavItem(
            "Profile",
            Routes.FarmerProfile.route,
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