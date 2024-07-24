package com.example.evvolicatalogue.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel

@Composable
fun CreateProductScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    productImageViewModel: ProductImageViewModel,
    createNewProduct: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    val productName by productViewModel.productName.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val imageUri by productViewModel.imageUri.collectAsState()

    val categories = categoryViewModel.categories.collectAsLazyPagingItems()

    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            productViewModel.onImageUriSelected(uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = productName,
            onValueChange = productViewModel::onProductNameChange,
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                text = selectedCategory?.name ?: "Select Category",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.itemSnapshotList.items.forEach { category ->
                    print(category)
                    DropdownMenuItem(
                        text = {Text(text = category.name)},
                        onClick = {
                            productViewModel.onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Create New Category") },
                    onClick = { navHostController.navigate(Screen.CreateCategoryScreen.route ) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = if (imageUri != null) "Change Image" else "Upload Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newProduct = ProductEntity(
                    categoryId = selectedCategory?.id ?: 0,
                    type = null, // Set other required fields appropriately
                    typeRu = null,
                    code = "", // Set appropriate code
                    model = null,
                    title = productName,
                    titleRu = "", // Set appropriate titleRu
                    description = null,
                    descriptionRu = null,
                    imageUrl = imageUri.toString()
                )
                createNewProduct(newProduct)
                // Add code to upload product image and create ProductImageEntity
                if (imageUri != null) {
                    val newProductImage = ProductImageEntity(
                        productId = newProduct.id, // Make sure to get the correct productId after insertion
                        imageUrl = imageUri.toString(),
                        thumbnailUrl = imageUri.toString() // Set appropriate thumbnail URL
                    )
                    productImageViewModel.insertProductImage(newProductImage)
                }
                navHostController.popBackStack()
            },
            enabled = productName.isNotEmpty() && selectedCategory != null && imageUri != null
        ) {
            Text("Add Product")
        }
    }
}




fun validateForm(
    name: String,
    phone: String,
    address: String,
): Boolean {
    return name.isNotEmpty() && address.isNotEmpty()
}





