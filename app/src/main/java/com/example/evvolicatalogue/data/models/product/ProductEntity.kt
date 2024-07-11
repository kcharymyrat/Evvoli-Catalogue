package com.example.evvolicatalogue.data.models.product

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.evvolicatalogue.data.models.category.CategoryEntity

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductEntity(
    @PrimaryKey val id: String,
    val categoryId: Long,
    val type: String,
    val typeEn: String,
    val typeRu: String,
    val model: String,
    val title: String,
    val titleEn: String,
    val titleRu: String,
    val description: String?,
    val descriptionEn: String?,
    val descriptionRu: String?,
    val imageUrl: String,
)
