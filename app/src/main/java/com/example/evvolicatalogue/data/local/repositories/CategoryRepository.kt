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
    fun getCategories(): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { categoryDao.getCategories() }
        ).flow
    }

    fun getOrderedCategories(orderBy: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
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
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { categoryDao.searchCategories(query) }
        ).flow
    }

    fun filterCategories(columnName: String, query: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { categoryDao.filterCategories(columnName, query) }
        ).flow
    }

    fun filterCategories(
        name: String?,
        nameRu: String?
    ): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { categoryDao.filterCategories(name, nameRu) }
        ).flow
    }

    fun getCategoriesWithProducts(): Flow<PagingData<CategoryWithProducts>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
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

    suspend fun deleteCategoryById(id: Int) {
        categoryDao.deleteCategoryById(id)
    }

    suspend fun getCategoryWithProducts(id: Int) {
        categoryDao.getCategoryWithProducts(id)
    }
}
