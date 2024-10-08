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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.PrimaryKey
import coil.compose.rememberAsyncImagePainter
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import com.example.evvolicatalogue.data.local.entities.ProductWithImages
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductUpdateDeleteScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    productImageViewModel: ProductImageViewModel,
    product: StateFlow<ProductWithImages?>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val productWithImages by product.collectAsState()

    var originalImageUri by remember { mutableStateOf<Uri?>(null) }
    var originalAdditionalImageUris by remember { mutableStateOf<List<Triple<Uri, String, Int>>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var imageToDelete by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(productWithImages) {
        productWithImages?.let {
            productViewModel.onProductTypeChange(it.product.type ?: "")
            productViewModel.onProductTypeRuChange(it.product.typeRu ?: "")
            productViewModel.onProductCodeChange(it.product.code)
            productViewModel.onProductModelChange(it.product.model ?: "")
            productViewModel.onProductTitleChange(it.product.title)
            productViewModel.onProductTitleRuChange(it.product.titleRu)
            productViewModel.onProductDescriptionChange(it.product.description ?: "")
            productViewModel.onProductDescriptionRuChange(it.product.descriptionRu ?: "")
            it.product.categoryId.let { categoryId ->
                categoryViewModel.getCategoryById(categoryId)?.let { category ->
                    productViewModel.onCategorySelected(category)
                }
            }
            it.product.imageUrl.let { imageUrl ->
                val uri = Uri.parse(imageUrl)
                productViewModel.onImageUriSelected(uri)
                originalImageUri = uri
            }

            productImageViewModel.fetchProductImages(productId = it.product.id)
            productImageViewModel.getMaxProductImageId()
        }
    }

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
    val isCodeUnique by productViewModel.isCodeUnique.collectAsState()
    val imageUris by productViewModel.imageUris.collectAsState()

    val categories = categoryViewModel.categories.collectAsLazyPagingItems()
    val maxProductImageId by productImageViewModel.maxProductImageId.collectAsState()

    var isTitleValid by remember { mutableStateOf(true) }
    var isTitleRuValid by remember { mutableStateOf(true) }
    var isCategorySelected by remember { mutableStateOf(true) }
    var isCodeValid by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }

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
                        value = selectedCategory?.name ?: stringResource(R.string.select_category),
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
                            text = { Text(stringResource(R.string.create_new_category)) },
                            onClick = { navHostController.navigate(Screen.CategoryCreateScreen.route) }
                        )
                    }
                }
            }
            if (!isCategorySelected) {
                Text(stringResource(R.string.select_a_category),  color = Color.Red)
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
                Text(stringResource(R.string.field_cannot_be_blank), color = Color.Red)
            } else if (!isCodeUnique) {
                Text(stringResource(R.string.code_must_be_unique), color = Color.Red)
            }
        }

        item {
            OutlinedTextField(
                value = productModel,
                onValueChange = productViewModel::onProductModelChange,
                label = { Text("Haryt modeli") },
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
                Text(stringResource(R.string.field_cannot_be_blank), color = Color.Red)
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
                Text(stringResource(R.string.field_cannot_be_blank), color = Color.Red)
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
                    modifier = Modifier
                        .size(128.dp)
                        .padding(8.dp)
                )
            }
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = if (imageUri != null) stringResource(R.string.change_main_image) else stringResource(
                    R.string.upload_main_image))
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(stringResource(R.string.additional_images))
        }

        item {
            productWithImages?.images?.forEach {productImage ->
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(model = productImage.imageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(128.dp).padding(8.dp)
                    )
                    Button(
                        onClick = {
                            // Remove the image from the database
                            productImageViewModel.deleteProductImage(productImage)

                            // Update the product state to remove the deleted image
                            productViewModel.removeImageFromProduct(productImage)

                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        item {
            imageUris.forEachIndexed { _, (uri, description) ->
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier.size(128.dp).padding(8.dp)
                    )
                    Button(
                        onClick = { productViewModel.removeImageUri(uri) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        item {
            Button(onClick = { additionalImageLauncher.launch("image/*") }) {
                Text(stringResource(R.string.add_additional_image))
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

                    if (isValid) {
                        val newImageUri = if (imageUri != originalImageUri) {
                            imageUri?.let { saveImageToInternalStorage(it, context) }
                        } else {
                            productWithImages?.product?.imageUrl
                        }

                        productWithImages?.product?.id?.let {
                            val updatedProduct = ProductEntity(
                                id = it,
                                categoryId = selectedCategory!!.id,
                                type = productType.ifBlank { null },
                                typeRu = productTypeRu.ifBlank { null },
                                code = productCode,
                                model = productModel.ifBlank { null },
                                title = productTitle,
                                titleRu = productTitleRu,
                                description = productDescription.ifBlank { null },
                                descriptionRu = productDescriptionRu.ifBlank { null },
                                imageUrl = newImageUri ?: ""
                            )
                            productViewModel.updateProduct(updatedProduct)

                            val newProductImages = imageUris.mapIndexed { index, (uri, description) ->
                                val id = maxProductImageId + 1 + index
                                ProductImageEntity(
                                    id = id, // Use the incremented id
                                    productId = updatedProduct.id,
                                    imageUrl = saveImageToInternalStorage(uri, context),
                                    description = description
                                )
                            }

                            newProductImages.forEach { newProductImage ->
                                productImageViewModel.insertProductImage(newProductImage)
                            }

                            productViewModel.clearSelectedCategory()
                            productViewModel.clearProductType()
                            productViewModel.clearProductTypeRu()
                            productViewModel.clearProductCode()
                            productViewModel.clearProductModel()
                            productViewModel.clearProductTitle()
                            productViewModel.clearProductTitleRu()
                            productViewModel.clearProductDescription()
                            productViewModel.clearProductDescriptionRu()
                            productViewModel.clearImageUri()
                            productViewModel.clearImageUris()

                            navHostController.popBackStack()
                        }

                    } else {
                        isTitleValid = productTitle.isNotBlank()
                        isTitleRuValid = productTitleRu.isNotBlank()
                        isCategorySelected = selectedCategory != null
                    }
                },
                enabled = productTitle.isNotEmpty() && selectedCategory != null
            ) {
                Text(stringResource(R.string.update_product))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    productWithImages?.product?.let {
                        productViewModel.deleteProduct(it)
                    }
                    navHostController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(stringResource(R.string.delete_product))
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

