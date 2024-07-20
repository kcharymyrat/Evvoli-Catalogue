package com.example.evvolicatalogue.navigation

import com.example.evvolitm.ui.screens.CategoriesScreen
//import com.example.evvolitm.ui.screens.CategoryProductsScreen
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
import com.example.evvolitm.presentation.CategoryScreenState
import com.example.evvolitm.presentation.MainViewModel
import com.example.evvolitm.presentation.ProductImageScreenState
import com.example.evvolitm.presentation.ProductScreenState
import com.example.evvolitm.ui.screens.AboutScreen
import com.example.evvolitm.ui.screens.CartItemsScreen
import com.example.evvolitm.ui.screens.CategoryProductsScreen
import com.example.evvolitm.ui.screens.EvvoliVelutoScreen
import com.example.evvolitm.ui.screens.LanguageSelectionScreen
import com.example.evvolitm.ui.screens.OrderForm
import com.example.evvolitm.ui.screens.ProductDetailScreen
import com.example.evvolitm.ui.screens.SearchProductsScreen
import com.example.evvolitm.ui.screens.SuccessScreen
import com.example.evvolitm.util.Screen

@Composable
fun Navigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String,
    categoryScreenState: CategoryScreenState,
    productScreenState: ProductScreenState,
    searchProductScreenState: ProductScreenState,
    productDetailScreenState: ProductImageScreenState,
    cartScreenState: CartScreenState,
    orderStatus: OrderStatus,
) {
    var query by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Screen.EvvoliAndVelutoScreen.route) {
            EvvoliVelutoScreen(
                navController = navController,
                modifier = Modifier
            )
        }

        composable(route = Screen.CategoriesScreen.route) {
            CategoriesScreen(
                navController = navController,
                categoryScreenState = categoryScreenState,
                onEvent = mainViewModel::onCategoryScreenEvent,
                modifier = Modifier
            )
        }

        composable(
            route = "${Screen.CategoryProductsScreen.route}/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")

            LaunchedEffect(categoryId) {
                if (categoryId != null && categoryId != mainViewModel.productCategoryId) {
                    println("In - LaunchedEffect(categoryId = $categoryId)")
                    productScreenState.productList = emptyList()
                    productScreenState.page = 1
                    mainViewModel.loadCategoryProducts(categoryId = categoryId, true)
                    mainViewModel.productCategoryId = categoryId
                }
            }

            CategoryProductsScreen(
                navController = navController,
                categoryId = categoryId ?: "",
                productScreenState = productScreenState,
                onScreenProductEvent = mainViewModel::onProductScreenEvent,
                cartScreenState = cartScreenState,
                onUpdateCartAndItsState = mainViewModel::updateCart,
                modifier = Modifier,
            )
        }

        composable(
            route = "${Screen.SearchProductsScreen.route}/{q}",
            arguments = listOf(navArgument("q") { type = NavType.StringType })
        ) { backStackEntry ->
            val q = backStackEntry.arguments?.getString("q").toString()

            LaunchedEffect(q) {
                if (searchProductScreenState.query.isEmpty() || q.toString() != query) {
                    query = q.toString()
                    searchProductScreenState.productList = emptyList()
                    searchProductScreenState.page = 1
                    searchProductScreenState.query = q ?: ""
                    mainViewModel.loadSearchProducts(q ?: "",true)
                }
            }

            SearchProductsScreen(
                navController = navController,
                searchProductScreenState = searchProductScreenState,
                onSearchProductScreenEvent = mainViewModel::onSearchProductScreenEvent,
                cartScreenState = cartScreenState,
                onUpdateCartAndItsState = mainViewModel::updateCart,
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
                    productDetailScreenState.productDetail = null
                    productDetailScreenState.productImageList = emptyList()
                    productDetailScreenState.productSpecList = emptyList()
                    mainViewModel.loadProductDetail(productId = productId, true)
                }
            }

            ProductDetailScreen(
                navController = navController,
                productDetailScreenState = productDetailScreenState,
                cartScreenState = cartScreenState,
                onUpdateCartAndItsState = mainViewModel::updateCart,
            )
        }


        composable(
            route = Screen.CartScreen.route
        ) {
            CartItemsScreen(
                navController = navController,
                cartScreenState = cartScreenState,
                onUpdateCartAndItsState = mainViewModel::updateCart,
                onCreateNewCardScreenState = mainViewModel::createCartScreenState,
            )
        }

        composable(
            route = Screen.OrderScreen.route
        ) {
            OrderForm(
                navController = navController,
                orderStatus = orderStatus,
                createOrder = mainViewModel::createOrder,
                resetOrderStatus = mainViewModel::resetOrderStatusAndCartState,
                cartScreenState = cartScreenState,
                onCreateNewCardScreenState = mainViewModel::createCartScreenState,
            )
        }

        composable(
            route = Screen.SuccessOrderScreen.route
        ) {
            SuccessScreen()
        }

        composable(
            route = Screen.AboutScreen.route
        ) {
            AboutScreen()
        }

        composable(
            route = Screen.SettingsScreen.route
        ) {
            LanguageSelectionScreen(navController = navController)
        }
    }
}