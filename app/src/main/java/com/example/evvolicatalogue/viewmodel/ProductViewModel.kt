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

    private val _productType = MutableStateFlow("")
    val productType: StateFlow<String> get() = _productType

    private val _productTypeRu = MutableStateFlow("")
    val productTypeRu: StateFlow<String> get() = _productTypeRu

    private val _productCode = MutableStateFlow("")
    val productCode: StateFlow<String> get() = _productCode

    private val _productModel = MutableStateFlow("")
    val productModel: StateFlow<String> get() = _productModel

    private val _productTitle = MutableStateFlow("")
    val productTitle: StateFlow<String> get() = _productTitle

    private val _productTitleRu = MutableStateFlow("")
    val productTitleRu: StateFlow<String> get() = _productTitleRu

    private val _productDescription = MutableStateFlow("")
    val productDescription: StateFlow<String> get() = _productDescription

    private val _productDescriptionRu = MutableStateFlow("")
    val productDescriptionRu: StateFlow<String> get() = _productDescriptionRu

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> get() = _selectedCategory

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri

    private val _maxProductId = MutableStateFlow(0)
    val maxProductId: StateFlow<Int> get() = _maxProductId

    private val _isCodeUnique = MutableStateFlow(true)
    val isCodeUnique: StateFlow<Boolean> get() = _isCodeUnique

    private val _imageUris = MutableStateFlow<List<Pair<Uri, String>>>(emptyList())
    val imageUris: StateFlow<List<Pair<Uri, String>>> get() = _imageUris

    private val _imageTriples = MutableStateFlow<List<Triple<Uri, String, Int>>>(emptyList())
    val imageTriples: StateFlow<List<Triple<Uri, String, Int>>> get() = _imageTriples

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

    fun filterProductsByColumn(columnName: String, query: String) {
        viewModelScope.launch {
            productRepository.filterProductsByColumn(columnName, query)
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

    fun onProductTitleChange(newTitle: String) {
        _productTitle.value = newTitle
    }

    fun onProductTitleRuChange(newTitleRu: String) {
        _productTitleRu.value = newTitleRu
    }

    fun onProductDescriptionChange(newDescription: String) {
        _productDescription.value = newDescription
    }

    fun onProductDescriptionRuChange(newDescriptionRu: String) {
        _productDescriptionRu.value = newDescriptionRu
    }

    fun onProductTypeChange(newType: String) {
        _productType.value = newType
    }

    fun onProductTypeRuChange(newTypeRu: String) {
        _productTypeRu.value = newTypeRu
    }

    fun onProductCodeChange(newCode: String) {
        _productCode.value = newCode
    }

    fun onProductModelChange(newCode: String) {
        _productModel.value = newCode
    }

    fun onCategorySelected(category: CategoryEntity) {
        _selectedCategory.value = category
    }

    fun onImageUriSelected(uri: Uri) {
        _imageUri.value = uri
    }

    fun getMaxProductId() {
        viewModelScope.launch {
            _maxProductId.value  = productRepository.getMaxProductId()
        }
    }

    fun checkCodeUnique(code: String) {
        viewModelScope.launch {
            _isCodeUnique.value = productRepository.isCodeUnique(code)
        }
    }

    fun addImageUri(uri: Uri, description: String) {
        _imageUris.value += (uri to description)
    }

    fun removeImageUri(uri: Uri) {
        _imageUris.value = _imageUris.value.filterNot { it.first == uri }
    }

    fun updateImageUris(updatedList: List<Pair<Uri, String>>) {
        _imageUris.value = updatedList
    }

    fun addImageTriple(uri: Uri, description: String, id: Int) {
        _imageTriples.value += Triple(uri, description, id)
    }

    fun removeImageTriple(uri: Uri) {
        _imageTriples.value = _imageTriples.value.filterNot { it.first == uri }
    }

    fun updateImageTriples(updatedList: List<Triple<Uri, String, Int>>) {
        _imageTriples.value = updatedList
    }

    fun clearImageTriples() {
        _imageTriples.value = emptyList()
    }

    fun clearImageUris() {
        _imageUris.value = emptyList()
    }

}


