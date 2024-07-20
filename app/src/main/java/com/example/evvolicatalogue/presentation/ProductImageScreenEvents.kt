package com.example.evvolicatalogue.presentation

sealed class ProductImageScreenEvents {
    data class Refresh(val type: String) : ProductImageScreenEvents()
}
