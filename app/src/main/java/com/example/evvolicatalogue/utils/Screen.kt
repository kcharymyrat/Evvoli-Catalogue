package com.example.evvolicatalogue.utils

sealed class Screen(val route: String) {
    data object CategoriesScreen: Screen("categories") // start
    data object CategoryProductsScreen: Screen("category_products")
    data object ProductDetailScreen: Screen("product_detail")
    data object SearchProductsScreen: Screen("search")
    data object CategoryCreateScreen: Screen("create_category")
    data object ProductCreateScreen: Screen("create_product")
    data object NewProductImageScreen: Screen("new_product_image")
    data object AboutScreen: Screen("about")

    data object SettingsScreen: Screen("settings")
    data object LanguageSettingsScreen: Screen("language-settings")

    data object CategoriesUpdateDeleteScreen: Screen("categories_update_delete")
    data object CategoryUpdateDeleteScreen: Screen("category_update_delete")

    data object ProductsUpdateDeleteScreen: Screen("products_update_delete")
    data object ProductUpdateDeleteScreen: Screen("update_delete_product")
}
