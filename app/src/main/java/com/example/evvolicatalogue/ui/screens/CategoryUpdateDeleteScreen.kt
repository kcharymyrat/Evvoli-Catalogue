package com.example.evvolicatalogue.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.viewmodel.CategoryViewModel

@Composable
fun CategoryUpdateDeleteScreen(
    navHostController: NavHostController,
    categoryViewModel: CategoryViewModel,
    categoryId: Int,
    modifier: Modifier = Modifier
) {
    val category = categoryViewModel.getCategoryFlowById(categoryId).collectAsState(initial = null).value

    var updatedName by remember { mutableStateOf("") }
    var updatedNameRu by remember { mutableStateOf("") }
    var updatedDescription by remember { mutableStateOf("") }
    var updatedDescriptionRu by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        }
    }

    LaunchedEffect(category) {
        category?.let {
            updatedName = it.name
            updatedNameRu = it.nameRu
            updatedDescription = it.description ?: ""
            updatedDescriptionRu = it.descriptionRu ?: ""
            imageUri = Uri.parse(it.imageUrl)
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
                value = updatedName,
                onValueChange = { updatedName = it },
                label = { Text(stringResource(R.string.kategoria_ady)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = updatedNameRu,
                onValueChange = { updatedNameRu = it },
                label = { Text(stringResource(R.string.nazvaniya_kategoriyi)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = updatedDescription,
                onValueChange = { updatedDescription = it },
                label = { Text(stringResource(R.string.kategoriya_beyany)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = updatedDescriptionRu,
                onValueChange = { updatedDescriptionRu = it },
                label = { Text(stringResource(R.string.opisaniye_kategoriyi)) },
                modifier = Modifier.fillMaxWidth()
            )
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
                Text(
                    text = if (imageUri != null) stringResource(R.string.change_image) else stringResource(R.string.upload_image)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    onClick = {
                        categoryViewModel.updateCategory(
                            CategoryEntity(
                                id = categoryId,
                                name = updatedName,
                                nameRu = updatedNameRu,
                                description = updatedDescription.ifBlank { null },
                                descriptionRu = updatedDescriptionRu.ifBlank { null },
                                imageUrl = imageUri.toString()
                            )
                        )
                        navHostController.popBackStack()
                    }
                ) {
                    Text(stringResource(R.string.update_category), fontSize = 10.sp)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        showDialog = true // Show the confirmation dialog
                    }
                ) {
                    Text(stringResource(R.string.delete_category), fontSize = 10.sp)
                }
            }
        }
    }

    // Confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_category)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete)) },
            confirmButton = {
                Button(
                    onClick = {
                        categoryViewModel.deleteCategoryById(categoryId)
                        showDialog = false
                        navHostController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
