package com.example.evvolicatalogue.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.evvolicatalogue.data.local.dao.CategoryDao
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
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
}
