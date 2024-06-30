package com.example.agrishop.common.navigation

sealed class Routes(val route: String) {

    data object Home : Routes("home")
    data object Cart : Routes("cart")
    data object Inventory : Routes("inventory")
    data object Profile : Routes("profile")
    data object NotificationBuyer : Routes("notificationBuyer")

    data object BottomNavigation : Routes("bottomNavigation")
    data object BottomNavigation2 : Routes("bottomNavigation2")
    data object Login : Routes("login")
    data object Signup : Routes("signup")
    data object Splash : Routes("splash")

    data object Notification : Routes("notification")
    data object Dashboard : Routes("dashboard")
    data object Product : Routes("product")
    data object FarmerProfile : Routes("farmer_profile")


}
