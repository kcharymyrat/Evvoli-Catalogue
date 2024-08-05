package com.example.evvolicatalogue.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.evvolicatalogue.data.local.dao.ProductImageDao
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductImageRepository @Inject constructor(private val productImageDao: ProductImageDao) {
    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    fun getProductImagesByProductId(productId: Int): Flow<PagingData<ProductImageEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productImageDao.getProductImagesByProductId(productId) }
        ).flow
    }

    suspend fun insertProductImage(image: ProductImageEntity) {
        productImageDao.insertProductImage(image)
    }

    suspend fun updateProductImage(image: ProductImageEntity) {
        productImageDao.updateProductImage(image)
    }

    suspend fun deleteProductImage(image: ProductImageEntity) {
        productImageDao.deleteProductImage(image)
    }

    suspend fun getMaxProductImageId(): Int {
        return productImageDao.getMaxProductImageId()
    }

    fun getProductImageById(id: Int): ProductImageEntity? {
        return productImageDao.getProductImageById(id=id)
    }
}
