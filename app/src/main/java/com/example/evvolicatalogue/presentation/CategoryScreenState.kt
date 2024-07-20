package com.example.evvolicatalogue.presentation

import com.example.evvolitm.domain.model.Category

data class CategoryScreenState(
    var isLoading: Boolean = false,
    var page: Int = 1,
    var categoryList: List<Category> = emptyList(),
    var isRefreshing: Boolean = false,
    var hasError: Boolean = false

//    var previousConnectivityState: MutableState<String> = mutableStateOf(""),
//    var backOnlineStarted: Boolean = false,
)