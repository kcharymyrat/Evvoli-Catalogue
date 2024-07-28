package com.example.evvolicatalogue.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "categories",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["nameRu"], unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val nameRu: String,
    val description: String?,
    val descriptionRu: String?,
    val imageUrl: String,
)