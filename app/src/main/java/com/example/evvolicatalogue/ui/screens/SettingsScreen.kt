package com.example.evvolicatalogue.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.utils.Screen


@Composable
fun SettingsScreen(navHostController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = stringResource(R.string.select),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SettingOption(
                settingsName = stringResource(R.string.languages),
                icon = Icons.Default.Language,
                onSettingsSelected = {
                    navHostController.navigate(Screen.LanguageSettingsScreen.route)
                }
            )
        }

        item {
            SettingOption(
                settingsName = stringResource(R.string.add_new_product),
                icon = Icons.Default.Add,
                onSettingsSelected = {
                    navHostController.navigate(Screen.ProductCreateScreen.route)
                }
            )
        }

        item {
            SettingOption(
                settingsName = stringResource(R.string.categories_update_delete),
                icon = Icons.Default.Category,
                onSettingsSelected = {
                    navHostController.navigate(Screen.CategoriesUpdateDeleteScreen.route)
                }
            )
        }

    }
}

@Composable
fun SettingOption(
    settingsName: String,
    icon: ImageVector,
    onSettingsSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSettingsSelected() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = settingsName, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

