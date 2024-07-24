package com.example.evvolicatalogue.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["code"], unique = true),
        Index(value = ["model"], unique = true)
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val type: String?,
    val typeRu: String?,
    val code: String,
    val model: String?,
    val title: String,
    val titleRu: String,
    val description: String?,
    val descriptionRu: String?,
    val imageUrl: String,
)
