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
import com.example.evvolicatalogue.data.local.entities.CategoryWithProducts

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getCategories(): PagingSource<Int, CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryList(categoryList: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryItem(categoryEntity: CategoryEntity)

    @Update
    suspend fun updateCategoryItem(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteCategoryItem(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Int)

    @Query("SELECT * FROM categories ORDER BY :orderBy ASC")
    suspend fun getOrderedCategories(orderBy: String): List<CategoryEntity>

    @Query("SELECT * FROM categories ORDER BY :orderBy DESC")
    suspend fun getOrderedCategoriesDesc(orderBy: String): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE name LIKE :query || '%'")
    suspend fun searchCategories(query: String): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE :columnName LIKE :query || '%'")
    suspend fun filterCategories(columnName: String, query: String): List<CategoryEntity>

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getCategoriesWithProducts(): List<CategoryWithProducts>
}

