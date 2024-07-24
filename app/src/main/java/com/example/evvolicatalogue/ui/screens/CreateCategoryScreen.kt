package com.example.evvolicatalogue.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.viewmodel.CategoryViewModel

@Composable
fun CreateCategoryScreen(
    navHostController: NavHostController,
    categoryViewModel: CategoryViewModel,
    modifier: Modifier = Modifier
) {
    val categoryName by categoryViewModel.categoryName.collectAsState()
    val categoryNameRu by categoryViewModel.categoryNameRu.collectAsState()
    val categoryDescription by categoryViewModel.categoryDescription.collectAsState()
    val categoryDescriptionRu by categoryViewModel.categoryDescriptionRu.collectAsState()
    val categoryImageUri by categoryViewModel.categoryImageUri.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            categoryViewModel.onCategoryImageUriSelected(uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = categoryName,
            onValueChange = categoryViewModel::onCategoryNameChange,
            label = { Text("Category Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = categoryNameRu,
            onValueChange = categoryViewModel::onCategoryNameRuChange,
            label = { Text("Category Name (RU)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = categoryDescription,
            onValueChange = categoryViewModel::onCategoryDescriptionChange,
            label = { Text("Category Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = categoryDescriptionRu,
            onValueChange = categoryViewModel::onCategoryDescriptionRuChange,
            label = { Text("Category Description (RU)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = if (categoryImageUri != null) "Change Image" else "Upload Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newCategory = CategoryEntity(
                    name = categoryName,
                    nameRu = categoryNameRu,
                    description = categoryDescription,
                    descriptionRu = categoryDescriptionRu,
                    imageUrl = categoryImageUri.toString()
                )
                categoryViewModel.insertCategory(newCategory)
                navHostController.popBackStack()
            },
            enabled = categoryName.isNotEmpty() && categoryImageUri != null
        ) {
            Text("Add Category")
        }
    }
}

