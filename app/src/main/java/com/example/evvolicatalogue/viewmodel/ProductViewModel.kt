package com.example.evvolicatalogue.viewmodel

import androidx.lifecycle.*
import com.example.evvolicatalogue.data.local.dao.ProductDao
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productDao: ProductDao
) : ViewModel() {

    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> get() = _products

    init {
        viewModelScope.launch {
            _products.value = productDao.getProducts()
        }
    }

    fun insertProduct(product: ProductEntity) = viewModelScope.launch {
        productDao.insertProduct(product)
        _products.value = productDao.getProducts()
    }

    fun updateProduct(product: ProductEntity) = viewModelScope.launch {
        productDao.updateProductItem(product)
        _products.value = productDao.getProducts()
    }

    fun deleteProduct(product: ProductEntity) = viewModelScope.launch {
        productDao.deleteProductItem(product)
        _products.value = productDao.getProducts()
    }
}


