package com.example.evvolicatalogue.viewmodel

import androidx.lifecycle.*
import com.example.evvolicatalogue.data.local.dao.CategoryDao
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryEntity>>()
    val categories: LiveData<List<CategoryEntity>> get() = _categories

    init {
        viewModelScope.launch {
            _categories.value = categoryDao.getCategories()
        }
    }

    fun insertCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryDao.insertCategoryItem(category)
        _categories.value = categoryDao.getCategories()
    }

    fun updateCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryDao.updateCategoryItem(category)
        _categories.value = categoryDao.getCategories()
    }

    fun deleteCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryDao.deleteCategoryItem(category)
        _categories.value = categoryDao.getCategories()
    }
}