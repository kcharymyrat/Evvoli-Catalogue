package com.example.evvolicatalogue.data.models.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.example.evvolicatalogue.data.models.product.ProductEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryList(categoryList: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryItem(categoryEntity: CategoryEntity)

    @Update
    suspend fun updateCategoryItem(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: String)

    @Query("SELECT * FROM categories ORDER BY :orderBy ASC")
    suspend fun getOrderedCategories(orderBy: String): List<CategoryEntity>

    @Query("SELECT * FROM categories ORDER BY :orderBy DESC")
    suspend fun getOrderedCategoriesDesc(orderBy: String): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE name LIKE :query || '%'")
    suspend fun searchCategories(query: String): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE :columnName LIKE :query || '%'")
    suspend fun filterCategories(columnName: String, query: String): List<CategoryEntity>

    // Relationship queries (assuming a Product entity with categoryId foreign key)
    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getCategoriesWithProducts(): List<CategoryWithProducts>
}

data class CategoryWithProducts(
    val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val products: List<ProductEntity>
)