package com.example.agrishop.farmer.composables

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.agrishop.farmer.viewmodel.AddProductViewModel
import kotlinx.coroutines.launch


@Composable
fun AddProductScreen(
    onProductAdded: () -> Unit,
    viewModel: AddProductViewModel = viewModel()
) {

    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var productImageUri by remember { mutableStateOf<Uri?>(null) }
    var farmName by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {

        viewModel.getFarmName {
            farmName = it
        }
    }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            productImageUri = uri
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Product Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PriceVisualTransformation()

        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = productStock,
            onValueChange = { productStock = it },
            label = { Text("Product Stock") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = WeightVisualTransformation()

        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text("Pick Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        productImageUri?.let {
            Card(
                modifier = Modifier.size(100.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)

                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                if (productName.isNotEmpty() && productDescription.isNotEmpty() && productPrice.isNotEmpty() && productStock.isNotEmpty() && productImageUri != null) {
                    farmName?.let {
                        viewModel.addProduct(
                            context,
                            productName,
                            productDescription,
                            productPrice.toDouble(),
                            productStock.toInt(),
                            productImageUri!!,
                            it
                        )
                    }
                    Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT)
                        .show()
                    onProductAdded()
                    productName = ""
                    productDescription = ""
                    productPrice = ""
                    productStock = ""
                    productImageUri = null
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
                //  }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text("Add Product")
        }
    }
}

class PriceVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text + "   rs/kg"),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return if (offset <= text.text.length) offset else text.text.length
                }
            }
        )
    }
}

class WeightVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text + "   kg"),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return if (offset <= text.text.length) offset else text.text.length
                }
            }
        )
    }
}