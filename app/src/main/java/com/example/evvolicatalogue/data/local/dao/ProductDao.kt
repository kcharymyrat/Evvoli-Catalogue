package com.example.evvolicatalogue.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductWithImages

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getProducts(): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductList(categoryList: List<ProductEntity>)

    @Update
    suspend fun updateProductItem(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProductItem(productEntity: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM products ORDER BY :orderBy ASC")
    suspend fun getOrderedProducts(orderBy: String): List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY :orderBy DESC")
    suspend fun getOrderedProductsDesc(orderBy: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE name LIKE :query || '%'")
    suspend fun searchProducts(query: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE :columnName LIKE :query || '%'")
    suspend fun filterProducts(columnName: String, query: String): List<ProductEntity>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductWithSpecs(productId: Int): ProductWithImages
}

