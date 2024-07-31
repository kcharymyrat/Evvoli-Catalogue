package com.example.evvolicatalogue.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCreateScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    productImageViewModel: ProductImageViewModel,
    createNewProduct: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    val productType by productViewModel.productType.collectAsState()
    val productTypeRu by productViewModel.productTypeRu.collectAsState()
    val productCode by productViewModel.productCode.collectAsState()
    val productModel by productViewModel.productModel.collectAsState()
    val productTitle by productViewModel.productTitle.collectAsState()
    val productTitleRu by productViewModel.productTitleRu.collectAsState()
    val productDescription by productViewModel.productDescription.collectAsState()
    val productDescriptionRu by productViewModel.productDescriptionRu.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val imageUri by productViewModel.imageUri.collectAsState()
    val maxProductId by productViewModel.maxProductId.collectAsState()
    val isCodeUnique by productViewModel.isCodeUnique.collectAsState()
    val imageUris by productViewModel.imageUris.collectAsState()

    val categories = categoryViewModel.categories.collectAsLazyPagingItems()
    val maxProductImageId by productImageViewModel.maxProductImageId.collectAsState()

    var isTitleValid by remember { mutableStateOf(true) }
    var isTitleRuValid by remember { mutableStateOf(true) }
    var isCategorySelected by remember { mutableStateOf(true) }
    var isCodeValid by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productViewModel.getMaxProductId()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            productViewModel.onImageUriSelected(uri)
        }
    }

    val additionalImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            productViewModel.addImageUri(uri, "")
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Box(
                contentAlignment = Alignment.Center
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .border(
                                width = 1.dp,
                                color = if (isCategorySelected) Color.Transparent else Color.Red,
                                shape = MaterialTheme.shapes.small
                            )
                    )

                    ExposedDropdownMenu(
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
                            onClick = { navHostController.navigate(Screen.CategoryCreateScreen.route) }
                        )
                    }
                }
            }
            if (!isCategorySelected) {
                Text("Please select a category", color = Color.Red)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            OutlinedTextField(
                value = productType,
                onValueChange = productViewModel::onProductTypeChange,
                label = { Text("Haryt görnüşi") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = productTypeRu,
                onValueChange = productViewModel::onProductTypeRuChange,
                label = { Text("Тип продукта") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = productCode,
                onValueChange = {
                    productViewModel.onProductCodeChange(it)
                    isCodeValid = it.isNotBlank()
                    productViewModel.checkCodeUnique(it)
                },
                label = { Text("Haryt kody") },
                isError = !isCodeValid || !isCodeUnique,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isCodeValid) {
                Text("Field cannot be blank", color = Color.Red) // TODO: translate
            } else if (!isCodeUnique) {
                Text("Code must be unique", color = Color.Red) // TODO: translate
            }
        }
        item {
            OutlinedTextField(
                value = productModel,
                onValueChange = productViewModel::onProductModelChange,
                label = { Text("Haryt modeli") }, // TODO: translation
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = productTitle,
                onValueChange = {
                    productViewModel.onProductTitleChange(it)
                    isTitleValid = it.isNotBlank()
                },
                label = { Text("Haryt ady") },
                isError = !isTitleValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isTitleValid) {
                Text("Field cannot be blank", color = Color.Red) // TODO: translate
            }
        }
        item {
            OutlinedTextField(
                value = productTitleRu,
                onValueChange = {
                    productViewModel.onProductTitleRuChange(it)
                    isTitleRuValid = it.isNotBlank()
                },
                label = { Text("Название продукта") },
                isError = !isTitleRuValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isTitleRuValid) {
                Text("Field cannot be blank", color = Color.Red) // TODO: translate
            }
        }
        item {
            OutlinedTextField(
                value = productDescription,
                onValueChange = productViewModel::onProductDescriptionChange,
                label = { Text("Haryt beýany") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = productDescriptionRu,
                onValueChange = productViewModel::onProductDescriptionRuChange,
                label = { Text("Описание продукта") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp).padding(8.dp)
                )
            }

            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = if (imageUri != null) "Change Main Image" else "Upload Main Image")
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text("Additional Images", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            imageUris.forEachIndexed { index, (uri, description) ->
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier.size(128.dp).padding(8.dp)
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { newDescription ->
                            val updatedList = imageUris.toMutableList()
                            updatedList[index] = uri to newDescription
                            productViewModel.updateImageUris(updatedList)
                        },
                        label = { Text("Image Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { productViewModel.removeImageUri(uri) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Remove")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        item {
            Button(onClick = { additionalImageLauncher.launch("image/*") }) {
                Text("Add Additional Image")
            }
        }


        item {
            Spacer(modifier = Modifier.height(16.dp))
        }


        item {
            Button(
                onClick = {
                    val isValid = validateInputs(
                        productTitle = productTitle,
                        productTitleRu = productTitleRu,
                        selectedCategory = selectedCategory
                    )
                    if (isValid && imageUri != null) {
                        val newProduct = ProductEntity(
                            id = maxProductId + 1,
                            categoryId = selectedCategory!!.id,
                            type = productType.ifBlank { null },
                            typeRu = productTypeRu.ifBlank { null },
                            code = productCode,
                            model = productModel.ifBlank { null },
                            title = productTitle,
                            titleRu = productTitleRu,
                            description = productDescription.ifBlank { null },
                            descriptionRu = productDescriptionRu.ifBlank { null },
                            imageUrl = saveImageToInternalStorage(imageUri!!, context)
                        )
                        createNewProduct(newProduct)

                        imageUris.forEach { (uri, description) ->
                            val newProductImage = ProductImageEntity(
                                id = maxProductImageId + 1,
                                productId = newProduct.id,
                                imageUrl = saveImageToInternalStorage(uri, context),
                                description = description
                            )
                            productImageViewModel.insertProductImage(newProductImage)
                        }

                        productViewModel.clearImageUris()
                        navHostController.popBackStack()
                    } else {
                        isTitleValid = productTitle.isNotBlank()
                        isTitleRuValid = productTitleRu.isNotBlank()
                        isCategorySelected = selectedCategory != null
                    }
                },
                enabled = productTitle.isNotEmpty() && selectedCategory != null && imageUri != null
            ) {
                Text("Add Product")
            }
        }
    }
}


private fun saveImageToInternalStorage(uri: Uri, context: Context): String {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "images")
    if (!file.exists()) {
        file.mkdirs()
    }
    val fileName = "${System.currentTimeMillis()}.jpg"
    val outputFile = File(file, fileName)
    val outputStream = FileOutputStream(outputFile)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return outputFile.absolutePath
}

private fun validateInputs(
    productTitle: String,
    productTitleRu: String,
    selectedCategory: CategoryEntity?
): Boolean {
    return productTitle.isNotBlank() && productTitleRu.isNotBlank() && selectedCategory != null
}





