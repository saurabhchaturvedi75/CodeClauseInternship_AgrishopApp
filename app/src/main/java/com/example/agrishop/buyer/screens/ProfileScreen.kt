package com.example.agrishop.buyer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.R
import com.example.agrishop.buyer.data.models.Buyer
import com.example.agrishop.buyer.viewmodel.BuyerViewModel
import com.example.agrishop.common.navigation.Routes
import com.example.agrishop.common.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(navController: NavHostController) {
    val buyerViewModel: BuyerViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val userData by buyerViewModel.userData.observeAsState(initial = null)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userData?.let { user ->
            Image(
                painter = painterResource(id = R.drawable.farmer), contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileContent(user)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0)
                    }
                },
            ) {
                Text("Logout")
            }
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun ProfileContent(user: Buyer) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Name: ${user.fullName}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Phone: ${user.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
    }
}
