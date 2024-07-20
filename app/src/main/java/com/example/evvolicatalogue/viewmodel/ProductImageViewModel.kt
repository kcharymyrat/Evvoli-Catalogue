package com.example.evvolicatalogue.viewmodel

import androidx.lifecycle.*
import com.example.evvolicatalogue.data.local.dao.ProductImageDao
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductImageViewModel @Inject constructor(
    private val productImageDao: ProductImageDao
) : ViewModel() {

    private val _productImages = MutableLiveData<List<ProductImageEntity>>()
    val productImages: LiveData<List<ProductImageEntity>> get() = _productImages

    fun insertProductImage(image: ProductImageEntity) = viewModelScope.launch {
        productImageDao.insertProductImage(image)
        // Update product images if necessary
    }

    fun updateProductImage(image: ProductImageEntity) = viewModelScope.launch {
        productImageDao.updateProductImage(image)
        // Update product images if necessary
    }

    fun deleteProductImage(image: ProductImageEntity) = viewModelScope.launch {
        productImageDao.deleteProductImage(image)
        // Update product images if necessary
    }

    fun getProductImagesByProductId(productId: Int) = viewModelScope.launch {
        _productImages.value = productImageDao.getProductImagesByProductId(productId)
    }
}