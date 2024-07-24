package com.example.evvolicatalogue.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.utils.Screen


@Composable
fun EvvoliTopBar(
    navHostController: NavHostController,
    currentRoute: String?,
    modifier: Modifier = Modifier,
) {
    val logoImage = if (isSystemInDarkTheme()) {
        painterResource(id = R.drawable.evvoli_logo_w1) // Image for dark theme
    } else {
        painterResource(R.drawable.evvoli_logo_b1) // Image for light theme
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Standard app bar height
            .padding(4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = logoImage,
                contentDescription = stringResource(R.string.evvoli_logo),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.logo_size))
                    .padding(vertical = dimensionResource(R.dimen.padding_small))
            )

            val currentScreenTitle = if (currentRoute == Screen.CategoriesScreen.route){
                stringResource(R.string.categories)
            } else if (currentRoute?.split("/")?.first() == Screen.CategoryProductsScreen.route) {
                stringResource(R.string.products)
            } else if (currentRoute?.split("/")?.first() == Screen.SearchProductsScreen.route) {
                stringResource(R.string.products_search)
            } else if (currentRoute?.split("/")?.first() == Screen.ProductDetailScreen.route) {
                stringResource(R.string.product_details)
            } else if (currentRoute == Screen.CreateCategoryScreen.route) {
                stringResource(R.string.new_category)
            } else if (currentRoute == Screen.CreateProductScreen.route) {
                stringResource(R.string.new_product)
            } else if (currentRoute == Screen.NewProductImageScreen.route) {
                stringResource(R.string.new_product_image)
            } else if (currentRoute == Screen.AboutScreen.route) {
                stringResource(R.string.evvoli_turkmenistan)
            } else if (currentRoute == Screen.SettingsScreen.route) {
                stringResource(R.string.settings)
            } else {
                ""
            }

            Text(
                text = currentScreenTitle,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = {
                    navHostController.navigate(Screen.SettingsScreen.route)
                },
                modifier = Modifier
                    .size(dimensionResource(R.dimen.logo_size))
                    .padding(vertical = dimensionResource(R.dimen.padding_small))
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")
            }
        }
    }
}
