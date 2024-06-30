package com.example.agrishop.farmer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.agrishop.farmer.composables.AddProductScreen
import com.example.agrishop.farmer.viewmodel.AddProductViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.agrishop.farmer.composables.ProductCard
import com.example.agrishop.farmer.viewmodel.FarmerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Product(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState()
    )
    val addProductViewModel: AddProductViewModel = viewModel()
    var showBottomSheet by remember { mutableStateOf(false) }
    val farmerViewModel: FarmerViewModel = viewModel()

    LaunchedEffect(Unit) {
        addProductViewModel.fetchProducts()
        farmerViewModel.checkAndLogLowStockNotifications()
    }


    val products by addProductViewModel.products.observeAsState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            AddProductScreen(onProductAdded = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                    showBottomSheet = false
                }
            })
        },
        sheetPeekHeight = 0.dp // Hide the peek height
    ) { innerPadding ->
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.hasPartiallyExpandedState) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                            showBottomSheet = true
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.hide()
                            showBottomSheet = false
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Show Bottom Sheet"
                    )
                }
            }
        ) { scaffoldPadding ->
            Spacer(modifier = Modifier.padding(scaffoldPadding))

            Box(
                modifier = Modifier
                    .fillMaxSize()

                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(Modifier.fillMaxHeight()) {
                    items(products.orEmpty().reversed()) { product ->
                        ProductCard(product)
                    }
                }
            }

        }
    }
}
