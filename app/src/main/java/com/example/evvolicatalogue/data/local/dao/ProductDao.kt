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

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    fun getProductsByCategoryId(categoryId: Int): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductList(productList: List<ProductEntity>)

    @Update
    suspend fun updateProductItem(product: ProductEntity)

    @Delete
    suspend fun deleteProductItem(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM products ORDER BY :orderBy ASC")
    fun getOrderedProducts(orderBy: String): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products ORDER BY :orderBy DESC")
    fun getOrderedProductsDesc(orderBy: String): PagingSource<Int, ProductEntity>

    @Query("""
        SELECT DISTINCT * FROM products
        WHERE title LIKE :query || '%' OR
              titleRu LIKE :query || '%' OR
              code LIKE :query || '%' OR
              model LIKE :query || '%' OR
              type LIKE :query || '%' OR
              typeRu LIKE :query || '%'
    """)
    fun searchProducts(query: String): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE :columnName LIKE :query || '%'")
    fun filterProductsByColumn(columnName: String, query: String): PagingSource<Int, ProductEntity>

    @Query("""
        SELECT * FROM products 
        WHERE (:categoryId IS NULL OR categoryId = :categoryId) 
        AND (:type IS NULL OR type LIKE :type || '%') 
        AND (:typeRu IS NULL OR typeRu LIKE :typeRu || '%') 
        AND (:code IS NULL OR code LIKE :code || '%') 
        AND (:model IS NULL OR model LIKE :model || '%') 
        AND (:title IS NULL OR title LIKE :title || '%') 
        AND (:titleRu IS NULL OR titleRu LIKE :titleRu || '%')
    """)
    fun filterProducts(
        categoryId: Int?,
        type: String?,
        typeRu: String?,
        code: String?,
        model: String?,
        title: String?,
        titleRu: String?
    ): PagingSource<Int, ProductEntity>

    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsWithImages(): PagingSource<Int, ProductWithImages>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductWithImages(productId: Int): ProductWithImages

    @Query("SELECT MAX(id) FROM products")
    suspend fun getMaxProductId(): Int
}

