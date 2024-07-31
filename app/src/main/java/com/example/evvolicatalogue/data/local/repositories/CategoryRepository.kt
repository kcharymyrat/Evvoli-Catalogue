package com.example.evvolicatalogue.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.evvolicatalogue.data.local.dao.CategoryDao
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.CategoryWithProducts
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {
    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    fun getCategories(): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { categoryDao.getCategories() }
        ).flow
    }

    fun getOrderedCategories(orderBy: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { categoryDao.getOrderedCategories(orderBy) }
        ).flow
    }

    fun getOrderedCategoriesDesc(orderBy: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { categoryDao.getOrderedCategoriesDesc(orderBy) }
        ).flow
    }

    fun searchCategories(query: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { categoryDao.searchCategories(query) }
        ).flow
    }

    fun filterCategoriesByColumn(columnName: String, query: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { categoryDao.filterCategoriesByColumn(columnName, query) }
        ).flow
    }

    fun filterCategories(
        name: String?,
        nameRu: String?
    ): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { categoryDao.filterCategories(name, nameRu) }
        ).flow
    }

    fun getCategoriesWithProducts(): Flow<PagingData<CategoryWithProducts>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { categoryDao.getCategoriesWithProducts() }
        ).flow
    }

    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategoryItem(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.updateCategoryItem(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategoryItem(category)
    }

    suspend fun getCategoryById(id: Int) {
        categoryDao.getCategoryById(id)
    }

    fun getCategoryFlowById(id: Int): Flow<CategoryEntity?> {
        return categoryDao.getCategoryFlowById(id)
    }

    suspend fun deleteCategoryById(id: Int) {
        categoryDao.deleteCategoryById(id)
    }

    suspend fun getMaxCategoryId(): Int {
        return categoryDao.getMaxCategoryId()
    }

    suspend fun isNameUnique(name: String): Boolean {
        return categoryDao.isNameUnique(name) == 0
    }

    suspend fun isNameRuUnique(nameRu: String): Boolean {
        return categoryDao.isNameRuUnique(nameRu) == 0
    }
}
