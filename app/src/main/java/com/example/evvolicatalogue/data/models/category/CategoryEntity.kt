package com.example.evvolicatalogue.data.models.category

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val nameEn: String,
    val nameRu: String,
    val description: String,
    val descriptionEn: String,
    val descriptionRu: String,
    val imageUrl: String,
)