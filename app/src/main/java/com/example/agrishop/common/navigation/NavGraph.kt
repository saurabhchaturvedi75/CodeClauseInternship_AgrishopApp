package com.example.agrishop.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.agrishop.buyer.composables.BottomNav
import com.example.agrishop.buyer.screens.Cart
import com.example.agrishop.buyer.screens.HomeScreen
import com.example.agrishop.buyer.screens.Inventory
import com.example.agrishop.buyer.screens.NotificationBuyerScreen
import com.example.agrishop.buyer.screens.ProfileScreen
import com.example.agrishop.common.screens.SignInScreen
import com.example.agrishop.common.screens.SignUpScreen
import com.example.agrishop.common.screens.Splash
import com.example.agrishop.farmer.composables.BottomNav2
import com.example.agrishop.farmer.screens.Dashboard
import com.example.agrishop.farmer.screens.FarmerProfile
import com.example.agrishop.farmer.screens.NotificationScreen
import com.example.agrishop.farmer.screens.Product

@Composable
fun NavGraph(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Routes.Splash.route) {


        composable(Routes.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.Inventory.route) {
            Inventory(navController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Routes.NotificationBuyer.route) {
            NotificationBuyerScreen(navController)
        }
        composable(Routes.Cart.route) {
            Cart(navController)
        }


        composable(Routes.Notification.route) {
            NotificationScreen(navController)
        }
        composable(Routes.Dashboard.route) {
            Dashboard(navController)
        }
        composable(Routes.Product.route) {
            Product(navController)
        }
        composable(Routes.FarmerProfile.route) {
            FarmerProfile(navController)
        }


        composable(Routes.BottomNavigation.route) {
            BottomNav(navController)
        }
        composable(Routes.BottomNavigation2.route) {
            BottomNav2(navController)
        }
        composable(Routes.Login.route) {
            SignInScreen(navController)
        }
        composable(Routes.Signup.route) {
            SignUpScreen(navController)
        }
        composable(Routes.Splash.route) {
            Splash(navController)
        }


    }
}