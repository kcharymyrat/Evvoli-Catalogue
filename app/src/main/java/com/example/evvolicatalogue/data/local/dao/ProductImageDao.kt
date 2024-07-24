package com.example.evvolicatalogue.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity

@Dao
interface ProductImageDao {
    @Query("SELECT * FROM product_images WHERE productId = :productId")
    fun getProductImagesByProductId(productId: Int): PagingSource<Int, ProductImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductImage(image: ProductImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductImageList(images: List<ProductImageEntity>)

    @Update
    suspend fun updateProductImage(image: ProductImageEntity)

    @Delete
    suspend fun deleteProductImage(image: ProductImageEntity)
}
