package com.example.evvolicatalogue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import com.example.evvolicatalogue.data.local.repositories.ProductImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductImageViewModel @Inject constructor(
    private val productImageRepository: ProductImageRepository
) : ViewModel() {

    private val _productImages = MutableStateFlow<PagingData<ProductImageEntity>>(PagingData.empty())
    val productImages: StateFlow<PagingData<ProductImageEntity>> get() = _productImages

    private val _productImageById = MutableStateFlow<ProductImageEntity?>(null)
    val productImageById: StateFlow<ProductImageEntity?> get() = _productImageById

    private val _maxProductImageId = MutableStateFlow(0)
    val maxProductImageId: StateFlow<Int> get() = _maxProductImageId

    private var newImageIdCounter = maxProductImageId.value + 1

    fun getNextImageId(): Int {
        return newImageIdCounter++
    }

    fun fetchProductImages(productId: Int) {
        viewModelScope.launch {
            productImageRepository.getProductImagesByProductId(productId)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _productImages.value = it
                }
        }
    }

    fun insertProductImage(image: ProductImageEntity) = viewModelScope.launch {
        productImageRepository.insertProductImage(image)
        fetchProductImages(image.productId)
    }

    fun updateProductImage(image: ProductImageEntity) = viewModelScope.launch {
        productImageRepository.updateProductImage(image)
        fetchProductImages(image.productId)
    }

    fun deleteProductImage(image: ProductImageEntity) = viewModelScope.launch {
        productImageRepository.deleteProductImage(image)
        fetchProductImages(image.productId)
    }

    fun getMaxProductImageId() {
        viewModelScope.launch {
            _maxProductImageId.value  = productImageRepository.getMaxProductImageId()
        }
    }

    fun updateCurrentProductImageById(id: Int) {
        viewModelScope.launch {
            _productImageById.value = productImageRepository.getProductImageById(id)
        }
    }
}
