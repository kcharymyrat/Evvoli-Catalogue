package com.example.evvolicatalogue.utils

sealed class Screen(val route: String) {
    data object CategoriesScreen: Screen("categories") // start
    data object CategoryProductsScreen: Screen("category_products")
    data object ProductDetailScreen: Screen("product_detail")
    data object SearchProductsScreen: Screen("search")
    data object NewCategoryScreen: Screen("new_category")
    data object NewProductScreen: Screen("new_product")
    data object NewProductImageScreen: Screen("new_product_image")
    data object AboutScreen: Screen("about")
    data object SettingsScreen: Screen("settings")
}
