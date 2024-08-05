package com.example.evvolicatalogue.data.local.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.evvolicatalogue.data.local.dao.ProductDao
import com.example.evvolicatalogue.data.local.entities.CategoryWithProducts
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductWithImages
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productDao: ProductDao) {
    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    fun getProducts(): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.getProducts() }
        ).flow
    }

    fun getProductsByCategoryId(categoryId: Int): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.getProductsByCategoryId(categoryId) }
        ).flow
    }

    fun getOrderedProducts(orderBy: String): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.getOrderedProducts(orderBy) }
        ).flow
    }

    fun getOrderedProductsDesc(orderBy: String): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.getOrderedProductsDesc(orderBy) }
        ).flow
    }

    fun searchProducts(query: String): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.searchProducts(query) }
        ).flow
    }

    fun filterProductsByColumn(columnName: String, query: String): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.filterProductsByColumn(columnName, query) }
        ).flow
    }

    fun filterProducts(
        categoryId: Int?,
        type: String?,
        typeRu: String?,
        code: String?,
        model: String?,
        title: String?,
        titleRu: String?
    ): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                productDao.filterProducts(
                    categoryId, type, typeRu, code, model, title, titleRu
                )
            }
        ).flow
    }

    fun getProductsWithImages(): Flow<PagingData<ProductWithImages>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { productDao.getProductsWithImages() }
        ).flow
    }

    suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProductItem(product)
    }

    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProductItem(product)
    }

    suspend fun getProductWithImages(id: Int): ProductWithImages {
        return productDao.getProductWithImages(id)
    }

    suspend fun getMaxProductId(): Int {
        return productDao.getMaxProductId()
    }

    suspend fun isCodeUnique(code: String): Boolean {
        return productDao.isCodeUnique(code) == 0
    }


}
