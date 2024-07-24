package com.example.evvolicatalogue.presentation


sealed class CategoryScreenEvents {
    data class OnPaginate(val type: String = "category"): CategoryScreenEvents()
    data object Refresh : CategoryScreenEvents()

//    object BackOnline : CategoryScreenEvents()
//    data class OnPaginate(val type: String) : CategoryScreenEvents()
//    object Navigate: CategoryScreenEvents
}