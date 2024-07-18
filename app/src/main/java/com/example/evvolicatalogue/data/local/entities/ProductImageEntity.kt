package com.example.evvolicatalogue.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "product_images",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("productId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductImageEntity(
    @PrimaryKey val id: Int,
    val productId: Int,
    val imageUrl: String,
    val thumbnailUrl: String,
    val description: String? = null
)

