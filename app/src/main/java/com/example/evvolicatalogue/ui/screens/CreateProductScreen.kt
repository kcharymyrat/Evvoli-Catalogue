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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    val productTitle by productViewModel.productTitle.collectAsState()
    val productTitleRu by productViewModel.productTitleRu.collectAsState()
    val productDescription by productViewModel.productDescription.collectAsState()
    val productDescriptionRu by productViewModel.productDescriptionRu.collectAsState()
    val productType by productViewModel.productType.collectAsState()
    val productTypeRu by productViewModel.productTypeRu.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val imageUri by productViewModel.imageUri.collectAsState()
    val maxProductId by productViewModel.maxProductId.collectAsState()

    val categories = categoryViewModel.categories.collectAsLazyPagingItems()
    val maxProductImageId by productImageViewModel.maxProductImageId.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            productViewModel.onImageUriSelected(uri)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    DropdownMenuItem(
                        text = { Text(text = category.name) },
                        onClick = {
                            productViewModel.onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Create New Category") },
                    onClick = { navHostController.navigate(Screen.CreateCategoryScreen.route) }
                )
            }
        }

        OutlinedTextField(
            value = productTitle,
            onValueChange = productViewModel::onProductTitleChange,
            label = { Text("Haryt ady") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = productTitleRu,
            onValueChange = productViewModel::onProductTitleRuChange,
            label = { Text("Название продукта") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = productDescription,
            onValueChange = productViewModel::onProductDescriptionChange,
            label = { Text("Haryt beýany") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = productDescriptionRu,
            onValueChange = productViewModel::onProductDescriptionRuChange,
            label = { Text("Описание продукта") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = productType,
            onValueChange = productViewModel::onProductTypeChange,
            label = { Text("Haryt görnüşi") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = productTypeRu,
            onValueChange = productViewModel::onProductTypeRuChange,
            label = { Text("Тип продукта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    DropdownMenuItem(
                        text = { Text(text = category.name) },
                        onClick = {
                            productViewModel.onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Create New Category") },
                    onClick = { navHostController.navigate(Screen.CreateCategoryScreen.route) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = if (imageUri != null) "Change Image" else "Upload Main Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newProduct = ProductEntity(
                    id = maxProductId + 1,
                    categoryId = selectedCategory?.id ?: 0,
                    type = productType,
                    typeRu = productTypeRu,
                    code = "PRODUCT_CODE_${maxProductId + 1}", // Example product code
                    model = "MODEL_${maxProductId + 1}", // Example model
                    title = productTitle,
                    titleRu = productTitleRu,
                    description = productDescription,
                    descriptionRu = productDescriptionRu,
                    imageUrl = imageUri.toString()
                )
                createNewProduct(newProduct)

                // Add code to upload product image and create ProductImageEntity
                if (imageUri != null) {
                    val newProductImage = ProductImageEntity(
                        id = maxProductImageId + 1,
                        productId = newProduct.id,
                        imageUrl = imageUri.toString(),
                    )
                    productImageViewModel.insertProductImage(newProductImage)
                }

                navHostController.popBackStack()
            },
            enabled = productTitle.isNotEmpty() && selectedCategory != null && imageUri != null,
            modifier = Modifier.fillMaxWidth(),
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





