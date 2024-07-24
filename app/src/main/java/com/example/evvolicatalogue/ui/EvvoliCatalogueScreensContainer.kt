package com.example.evvolicatalogue.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.evvolicatalogue.navigation.Navigation
import com.example.evvolicatalogue.ui.components.BottomNavigationBar
import com.example.evvolicatalogue.ui.components.CustomSearchBar
import com.example.evvolicatalogue.ui.components.EvvoliTopBar
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel

@Composable
fun EvvoliCatalogueScreensContainer(
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    productImageViewModel: ProductImageViewModel,
    navHostController: NavHostController,
    startDestination: String,
) {
    val categories= categoryViewModel.categories
    val categoryWithProducts = categoryViewModel.categoriesWithProducts.collectAsState().value

    val products = productViewModel.products
    val productWithImages = productViewModel.productWithImages

    val productImages= productImageViewModel.productImages.collectAsState().value

    val currentRoute = currentRoute(navHostController)

    Scaffold(
        topBar = {
            Column {
                EvvoliTopBar(navHostController = navHostController, currentRoute = currentRoute)

                // Conditionally display SearchScreen
                if (currentRoute == Screen.CategoriesScreen.route ||
                    currentRoute?.split("/")?.first() == Screen.CategoryProductsScreen.route ||
                    currentRoute?.split("/")?.first() == Screen.SearchProductsScreen.route)
                {
                    CustomSearchBar(navHostController)
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navHostController)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Navigation(
                navHostController = navHostController,
                startDestination = startDestination,
                categoryViewModel = categoryViewModel,
                productViewModel = productViewModel,
                productImageViewModel = productImageViewModel,
                categories = categories,
                categoryWithProducts = categoryWithProducts,
                products = products,
                productWithImages = productWithImages,
                productImages = productImages,
            )
        }
    }
}




@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
