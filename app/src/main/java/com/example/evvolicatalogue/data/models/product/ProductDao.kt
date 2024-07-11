package com.example.evvolicatalogue.data.models.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.evvolicatalogue.data.models.category.CategoryEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductList(categoryList: List<ProductEntity>)

    @Update
    suspend fun updateProductItem(productEntity: ProductEntity)

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductEntity>

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

