package com.example.evvolicatalogue.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evvolicatalogue.data.local.dao.CategoryDao
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.dao.ProductDao
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.dao.ProductImageDao
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity

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