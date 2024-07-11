package com.example.evvolicatalogue.data.models.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductList(categoryList: List<ProductEntity>)

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity

    @Query("SELECT * FROM products WHERE slug = :slug")
    suspend fun getProductBySlug(slug: String): ProductEntity?

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Query("DELETE FROM products WHERE categoryId = :categoryId")
    suspend fun deleteProductsByCategory(categoryId: String)

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductWithSpecsAndImages(productId: String): ProductEntity
}

