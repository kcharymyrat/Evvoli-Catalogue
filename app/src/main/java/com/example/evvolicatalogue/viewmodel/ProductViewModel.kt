package com.example.evvolicatalogue.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductWithImages
import com.example.evvolicatalogue.data.local.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<PagingData<ProductEntity>>(PagingData.empty())
    val products: StateFlow<PagingData<ProductEntity>> get() = _products

    private val _productWithImages = MutableStateFlow<ProductWithImages?>(null)
    val productWithImages: StateFlow<ProductWithImages?> get() = _productWithImages

    private val _productName = MutableStateFlow("")
    val productName: StateFlow<String> get() = _productName

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> get() = _selectedCategory

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            productRepository.getProducts()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun getProductsByCategoryId(categoryId: Int) {
        viewModelScope.launch {
            productRepository.getProductsByCategoryId(categoryId)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun insertProduct(product: ProductEntity) = viewModelScope.launch {
        productRepository.insertProduct(product)
        fetchProducts()
    }

    fun updateProduct(product: ProductEntity) = viewModelScope.launch {
        productRepository.updateProduct(product)
        fetchProducts()
    }

    fun deleteProduct(product: ProductEntity) = viewModelScope.launch {
        productRepository.deleteProduct(product)
        fetchProducts()
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            productRepository.searchProducts(query)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun filterProducts(columnName: String, query: String) {
        viewModelScope.launch {
            productRepository.filterProducts(columnName, query)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun filterProducts(
        categoryId: Int?,
        type: String?,
        typeRu: String?,
        code: String?,
        model: String?,
        title: String?,
        titleRu: String?
    ) {
        viewModelScope.launch {
            productRepository.filterProducts(
                categoryId, type, typeRu, code, model, title, titleRu
            ).cachedIn(viewModelScope).collectLatest {
                _products.value = it
            }
        }
    }

    fun getOrderedProducts(orderBy: String) {
        viewModelScope.launch {
            productRepository.getOrderedProducts(orderBy)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun getOrderedProductsDesc(orderBy: String) {
        viewModelScope.launch {
            productRepository.getOrderedProductsDesc(orderBy)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it
                }
        }
    }

    fun getProductWithImages(id: Int) {
        viewModelScope.launch {
            _productWithImages.value = productRepository.getProductWithImages(id)
        }
    }

    fun onProductNameChange(newName: String) {
        _productName.value = newName
    }

    fun onCategorySelected(category: CategoryEntity) {
        _selectedCategory.value = category
    }

    fun onImageUriSelected(uri: Uri) {
        _imageUri.value = uri
    }
}


