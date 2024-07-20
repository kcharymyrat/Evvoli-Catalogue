package com.example.evvolicatalogue.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel

@Composable
class EvvoliCatalogueScreensContainer(
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    productImageViewModel: ProductImageViewModel
    navController: NavHostController,
    startDestination: String,
) {

    val currentRoute = currentRoute(navController)

    Scaffold(
    topBar = {
        Column {
            EvvoliTopBar(navController = navController, currentRoute = currentRoute)

            // Conditionally display SearchScreen
            if (currentRoute == Screen.CategoriesScreen.route ||
                currentRoute?.split("/")?.first() == Screen.CategoryProductsScreen.route ||
                currentRoute?.split("/")?.first() == Screen.SearchProductsScreen.route)
            {
                CustomSearchBar(navController)
            }
        }
    },
    bottomBar = {
        BottomNavigationBar(navController, cartScreenState)
    }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Navigation(
                navController = navController,
                startDestination = startDestination,
                mainViewModel = mainViewModel,
                categoryScreenState = categoryScreenState,
                productScreenState = productScreenState,
                searchProductScreenState = searchProductScreenState,
                productDetailScreenState = productDetailScreenState,
                cartScreenState = cartScreenState,
                orderStatus = orderStatus,
            )
        }
    }
}


@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
