package com.example.evvolicatalogue.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import coil.compose.rememberAsyncImagePainter
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun CategoryCreateScreen(
    navHostController: NavHostController,
    categoryViewModel: CategoryViewModel,
    modifier: Modifier = Modifier
) {
    val categoryName by categoryViewModel.categoryName.collectAsState()
    val categoryNameRu by categoryViewModel.categoryNameRu.collectAsState()
    val categoryDescription by categoryViewModel.categoryDescription.collectAsState()
    val categoryDescriptionRu by categoryViewModel.categoryDescriptionRu.collectAsState()
    val categoryImageUri by categoryViewModel.categoryImageUri.collectAsState()
    val maxCategoryId by categoryViewModel.maxCategoryId.collectAsState()
    val isNameUnique by categoryViewModel.isNameUnique.collectAsState()
    val isNameRuUnique by categoryViewModel.isNameRuUnique.collectAsState()

    var isNameValid by remember { mutableStateOf(true) }
    var isNameRuValid by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        categoryViewModel.getMaxCategoryId()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            categoryViewModel.onCategoryImageUriSelected(uri)
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            OutlinedTextField(
                value = categoryName,
                onValueChange = {
                    categoryViewModel.onCategoryNameChange(it)
                    isNameValid = it.isNotBlank()
                    categoryViewModel.checkNameUnique(it)
                },
                label = { Text("Kategoriýanyň ady") },
                isError = !isNameValid || !isNameUnique,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isNameValid) {
                Text("Field cannot be blank", color = Color.Red) // TODO: translate
            } else if (!isNameUnique) {
                Text("Name must be unique", color = Color.Red) // TODO: translate
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            OutlinedTextField(
                value = categoryNameRu,
                onValueChange = {
                    categoryViewModel.onCategoryNameRuChange(it)
                    isNameRuValid = it.isNotBlank()
                    categoryViewModel.checkNameRuUnique(it)
                },
                label = { Text("Название категории") },
                isError = !isNameRuValid || !isNameRuUnique,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isNameRuValid) {
                Text("Field cannot be blank", color = Color.Red)
            } else if (!isNameRuUnique) {
                Text("Name must be unique", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            OutlinedTextField(
                value = categoryDescription,
                onValueChange = categoryViewModel::onCategoryDescriptionChange,
                label = { Text("Kategoriýa beýany") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            OutlinedTextField(
                value = categoryDescriptionRu,
                onValueChange = categoryViewModel::onCategoryDescriptionRuChange,
                label = { Text("Описание категории") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            if (categoryImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = categoryImageUri),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp).padding(8.dp)
                )
            }
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = if (categoryImageUri != null) "Change Image" else "Upload Image")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            println("maxCategoryId = $maxCategoryId")

            Button(
                onClick = {
                    if (categoryName.isNotBlank() && categoryNameRu.isNotBlank() &&
                        isNameUnique && isNameRuUnique && categoryImageUri != null) {
                        val newCategory = CategoryEntity(
                            id = maxCategoryId + 1,
                            name = categoryName,
                            nameRu = categoryNameRu,
                            description = categoryDescription.ifBlank { null },
                            descriptionRu = categoryDescriptionRu.ifBlank { null },
                            imageUrl = saveImageToInternalStorage(categoryImageUri!!, context)
                        )
                        categoryViewModel.insertCategory(newCategory)
                        navHostController.popBackStack()
                    } else {
                        isNameValid = categoryName.isNotBlank()
                        isNameRuValid = categoryNameRu.isNotBlank()
                    }
                },
                enabled = categoryImageUri != null
            ) {
                Text("Add Category")
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


fun validateCategoryForm(
    name: String,
    nameRu: String,
): Boolean {
    return name.isNotBlank() && nameRu.isNotBlank()
}