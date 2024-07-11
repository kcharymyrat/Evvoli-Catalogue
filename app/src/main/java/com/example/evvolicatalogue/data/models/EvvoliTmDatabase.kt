package com.example.evvolicatalogue.data.models

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evvolicatalogue.data.models.category.CategoryDao
import com.example.evvolicatalogue.data.models.category.CategoryEntity
import com.example.evvolicatalogue.data.models.product.ProductDao
import com.example.evvolicatalogue.data.models.product.ProductEntity
import com.example.evvolicatalogue.data.models.product.ProductImageDao
import com.example.evvolicatalogue.data.models.product.ProductImageEntity

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        ProductImageEntity::class,
    ],
    version = 1
)
abstract class EvvoliCatalogueDatabase: RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val productDao: ProductDao
    abstract val productImageDao: ProductImageDao
}