package com.example.evvolicatalogue.data.models.product

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithImages(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val images: List<ProductImageEntity>
)

