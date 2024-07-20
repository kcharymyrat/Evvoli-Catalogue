package com.example.evvolitm.presentation

sealed class ProductScreenEvents {
    data class OnPaginate(val type: String = ""): ProductScreenEvents()
    data object Refresh : ProductScreenEvents()

//    object BackOnline : CategoryScreenEvents()
//    data class OnPaginate(val type: String) : CategoryScreenEvents()
//    object Navigate: CategoryScreenEvents
}

