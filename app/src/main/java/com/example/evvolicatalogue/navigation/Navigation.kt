package com.example.evvolicatalogue.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.PagingData
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.CategoryWithProducts
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import com.example.evvolicatalogue.data.local.entities.ProductWithImages
import com.example.evvolicatalogue.ui.screens.AboutScreen
import com.example.evvolicatalogue.ui.screens.ProductCreateScreen
import com.example.evvolicatalogue.ui.screens.CategoriesScreen
import com.example.evvolicatalogue.ui.screens.CategoriesUpdateDeleteScreen
import com.example.evvolicatalogue.ui.screens.CategoryProductsScreen
import com.example.evvolicatalogue.ui.screens.CategoryCreateScreen
import com.example.evvolicatalogue.ui.screens.CategoryUpdateDeleteScreen
import com.example.evvolicatalogue.ui.screens.LanguageSelectionScreen
import com.example.evvolicatalogue.ui.screens.ProductDetailScreen
import com.example.evvolicatalogue.ui.screens.ProductUpdateDeleteScreen
import com.example.evvolicatalogue.ui.screens.ProductsUpdateDeleteScreen
import com.example.evvolicatalogue.ui.screens.SearchProductsScreen
import com.example.evvolicatalogue.ui.screens.SettingsScreen
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.StateFlow


@Composable
fun Navigation(
    navHostController: NavHostController,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    productImageViewModel: ProductImageViewModel,
    startDestination: String,
    categories: StateFlow<PagingData<CategoryEntity>>,
    categoryWithProducts: PagingData<CategoryWithProducts>,
    products: StateFlow<PagingData<ProductEntity>>,
    productWithImages: StateFlow<ProductWithImages?>,
    productImages: PagingData<ProductImageEntity>
) {
    var query by remember { mutableStateOf("") }

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(route = Screen.CategoriesScreen.route) {
            CategoriesScreen(
                navController = navHostController,
                categories = categories,
                modifier = Modifier
            )
        }

        composable(
            route = "${Screen.CategoryProductsScreen.route}/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")

            LaunchedEffect(categoryId) {
                if (categoryId != null) {
                    println("In - LaunchedEffect(categoryId = $categoryId)")
                    productViewModel.getProductsByCategoryId(categoryId = categoryId)
                }
            }

            CategoryProductsScreen(
                navHostController = navHostController,
                products = products,
                modifier = Modifier,
            )
        }

        composable(
            route = "${Screen.SearchProductsScreen.route}/{q}",
            arguments = listOf(navArgument("q") { type = NavType.StringType })
        ) { backStackEntry ->
            val q = backStackEntry.arguments?.getString("q").toString()


            LaunchedEffect(q) {
                if (q.toString().isNotBlank()) {
                    println("In - LaunchedEffect(q.toString = ${q.toString()})")
                    productViewModel.searchProducts(q)
                }
            }

            SearchProductsScreen(
                navHostController = navHostController,
                products = products,
                modifier = Modifier,
            )
        }

        composable(
            route = "${Screen.ProductDetailScreen.route}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")

            LaunchedEffect(productId) {
                if (productId != null) {
                    println("In - LaunchedEffect(productId = ${productId.toInt()})")
                    productViewModel.getProductWithImages(id = productId.toInt())
                }
            }

            ProductDetailScreen(
                navHostController = navHostController,
                product = productWithImages
            )
        }


        composable(
            route = Screen.CategoryCreateScreen.route
        ) {
            CategoryCreateScreen(
                navHostController = navHostController,
                categoryViewModel = categoryViewModel,
                modifier = Modifier,
            )
        }

        composable(
            route = Screen.AboutScreen.route
        ) {
            AboutScreen()
        }

        composable(
            route = Screen.SettingsScreen.route
        ) {
            SettingsScreen(navHostController = navHostController)
        }

        composable(
            route = Screen.LanguageSettingsScreen.route
        ) {
            LanguageSelectionScreen(navHostController = navHostController)
        }

        composable(
            route = Screen.LanguageSettingsScreen.route
        ) {
            LanguageSelectionScreen(navHostController = navHostController)
        }

        composable(
            route = Screen.ProductCreateScreen.route
        ) {
            ProductCreateScreen(
                navHostController = navHostController,
                productViewModel = productViewModel,
                categoryViewModel = categoryViewModel,
                productImageViewModel = productImageViewModel,
                createNewProduct = { newProduct ->
                    productViewModel.insertProduct(newProduct)
                },
                modifier = Modifier,
            )
        }

        composable(route = Screen.CategoriesUpdateDeleteScreen.route) {
            CategoriesUpdateDeleteScreen(
                navController = navHostController,
                categoryViewModel = categoryViewModel,
                categories = categories,
                modifier = Modifier
            )
        }

        composable(
            route = "${Screen.CategoryUpdateDeleteScreen.route}/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")

            LaunchedEffect(categoryId) {
                if (categoryId != null) {
                    println("In - LaunchedEffect(categoryId = $categoryId)")
                    productViewModel.getProductsByCategoryId(categoryId = categoryId)
                }
            }

            if (categoryId != null) {
                CategoryUpdateDeleteScreen(
                    navHostController = navHostController,
                    categoryViewModel = categoryViewModel,
                    categoryId = categoryId
                )
            }
        }

        composable(
            route = "${Screen.ProductsUpdateDeleteScreen.route}/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")

            LaunchedEffect(categoryId) {
                if (categoryId != null) {
                    println("In - LaunchedEffect(categoryId = $categoryId)")
                    productViewModel.getProductsByCategoryId(categoryId = categoryId)
                }
            }

            ProductsUpdateDeleteScreen(
                navHostController = navHostController,
                productViewModel = productViewModel,
                products = products,
                modifier = Modifier,
            )
        }

        composable(
            route = "${Screen.ProductUpdateDeleteScreen.route}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")

            LaunchedEffect(productId) {
                if (productId != null) {
                    println("In - LaunchedEffect(productId = ${productId.toInt()})")
                    productViewModel.getProductWithImages(id = productId.toInt())
                }
            }

            ProductUpdateDeleteScreen(
                navHostController = navHostController,
                productViewModel = productViewModel,
                categoryViewModel = categoryViewModel,
                productImageViewModel = productImageViewModel,
                product = productWithImages,
            )
        }

    }
}