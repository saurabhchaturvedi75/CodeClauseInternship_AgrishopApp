package com.example.agrishop.common.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.R
import com.example.agrishop.common.navigation.Routes
import com.example.agrishop.common.viewmodel.AuthViewModel
import com.example.agrishop.farmer.viewmodel.FarmerViewModel
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
     val firebaseUser by authViewModel.firebaseUser.observeAsState()
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoo),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
    }
    LaunchedEffect(true) {
        delay(3000)
        if (firebaseUser != null) {
            authViewModel.getUserType { userType ->
                if (userType != null) {
                    if (userType == "Farmer") {
                        navController.navigate(Routes.BottomNavigation2.route) {
                            popUpTo(Routes.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.BottomNavigation.route) {
                            popUpTo(Routes.Splash.route) { inclusive = true }
                        }
                    }

                } else {
                }
            }

        } else {
            navController.navigate(Routes.Login.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
}
