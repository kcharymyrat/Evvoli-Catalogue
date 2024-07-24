package com.example.evvolicatalogue.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.CategoryWithProducts
import com.example.evvolicatalogue.data.local.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<PagingData<CategoryEntity>>(PagingData.empty())
    val categories: StateFlow<PagingData<CategoryEntity>> get() = _categories

    private val _categoriesWithProducts = MutableStateFlow<PagingData<CategoryWithProducts>>(PagingData.empty())
    val categoriesWithProducts: StateFlow<PagingData<CategoryWithProducts>> get() = _categoriesWithProducts

    private val _categoryName = MutableStateFlow("")
    val categoryName: StateFlow<String> get() = _categoryName

    private val _categoryNameRu = MutableStateFlow("")
    val categoryNameRu: StateFlow<String> get() = _categoryNameRu

    private val _categoryDescription = MutableStateFlow("")
    val categoryDescription: StateFlow<String> get() = _categoryDescription

    private val _categoryDescriptionRu = MutableStateFlow("")
    val categoryDescriptionRu: StateFlow<String> get() = _categoryDescriptionRu


    private val _categoryImageUri = MutableStateFlow<Uri?>(null)
    val categoryImageUri: StateFlow<Uri?> get() = _categoryImageUri

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categories.value = it
                }
        }
    }

    fun insertCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryRepository.insertCategory(category)
        fetchCategories()
    }

    fun updateCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryRepository.updateCategory(category)
        fetchCategories()
    }

    fun deleteCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryRepository.deleteCategory(category)
        fetchCategories()
    }

    fun deleteCategoryById(id: Int) = viewModelScope.launch {
        categoryRepository.deleteCategoryById(id)
        fetchCategories()
    }

    fun searchCategories(query: String) {
        viewModelScope.launch {
            categoryRepository.searchCategories(query)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categories.value = it
                }
        }
    }

    fun filterCategories(columnName: String, query: String) {
        viewModelScope.launch {
            categoryRepository.filterCategories(columnName, query)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categories.value = it
                }
        }
    }

    fun filterCategories(name: String?, nameRu: String?) {
        viewModelScope.launch {
            categoryRepository.filterCategories(name, nameRu)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categories.value = it
                }
        }
    }

    fun getOrderedCategories(orderBy: String) {
        viewModelScope.launch {
            categoryRepository.getOrderedCategories(orderBy)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categories.value = it
                }
        }
    }

    fun getOrderedCategoriesDesc(orderBy: String) {
        viewModelScope.launch {
            categoryRepository.getOrderedCategoriesDesc(orderBy)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categories.value = it
                }
        }
    }

    fun getCategoriesWithProducts() {
        viewModelScope.launch {
            categoryRepository.getCategoriesWithProducts()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _categoriesWithProducts.value = it
                }
        }
    }

    fun onCategoryNameChange(newName: String) {
        _categoryName.value = newName
    }

    fun onCategoryNameRuChange(newNameRu: String) {
        _categoryNameRu.value = newNameRu
    }

    fun onCategoryDescriptionChange(newDescription: String) {
        _categoryDescription.value = newDescription
    }

    fun onCategoryDescriptionRuChange(newDescriptionRu: String) {
        _categoryDescriptionRu.value = newDescriptionRu
    }

    fun onCategoryImageUriSelected(uri: Uri) {
        _categoryImageUri.value = uri
    }
}

