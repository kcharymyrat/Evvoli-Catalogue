package com.example.evvolicatalogue.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.evvolicatalogue.data.local.dao.ProductDao
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productDao: ProductDao) {
    fun getProducts(): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { productDao.getProducts() }
        ).flow
    }
}
