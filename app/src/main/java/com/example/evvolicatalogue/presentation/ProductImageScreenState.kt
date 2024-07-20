package com.example.evvolitm.presentation

import com.example.evvolitm.domain.model.ProductDetail
import com.example.evvolitm.domain.model.ProductImage
import com.example.evvolitm.domain.model.ProductSpec

data class ProductImageScreenState(
    var isLoading: Boolean = false,
    var productDetail: ProductDetail? = null,
    var productImageList: List<ProductImage> = emptyList(),
    var productSpecList: List<ProductSpec> = emptyList()
)
