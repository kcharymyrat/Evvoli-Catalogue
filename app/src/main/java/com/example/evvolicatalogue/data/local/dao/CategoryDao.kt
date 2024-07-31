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
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryFlowById(categoryId: Int): Flow<CategoryEntity?>

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Int)

    @Query("SELECT * FROM categories ORDER BY :orderBy ASC")
    fun getOrderedCategories(orderBy: String): PagingSource<Int, CategoryEntity>

    @Query("SELECT * FROM categories ORDER BY :orderBy DESC")
    fun getOrderedCategoriesDesc(orderBy: String): PagingSource<Int, CategoryEntity>

    @Query("""
        SELECT DISTINCT * FROM categories
        WHERE name LIKE :query || '%' OR
              nameRu LIKE :query || '%'
    """)
    fun searchCategories(query: String): PagingSource<Int, CategoryEntity>

    @Query("SELECT * FROM categories WHERE :columnName LIKE :query || '%'")
    fun filterCategoriesByColumn(columnName: String, query: String): PagingSource<Int, CategoryEntity>


    @Query("""
        SELECT * FROM categories 
        WHERE (:name IS NULL OR name LIKE :name || '%') 
        AND (:nameRu IS NULL OR nameRu LIKE :nameRu || '%')
    """)
    fun filterCategories(
        name: String?,
        nameRu: String?
    ): PagingSource<Int, CategoryEntity>

    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithProducts(): PagingSource<Int, CategoryWithProducts>

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryWithProducts(categoryId: Int): CategoryWithProducts

    @Query("SELECT MAX(id) FROM categories")
    suspend fun getMaxCategoryId(): Int

    @Query("SELECT COUNT(*) FROM categories WHERE name = :name")
    suspend fun isNameUnique(name: String): Int

    @Query("SELECT COUNT(*) FROM categories WHERE nameRu = :nameRu")
    suspend fun isNameRuUnique(nameRu: String): Int
}

