package com.example.evvolicatalogue.data.models.category

import androidx.room.Embedded
import androidx.room.Relation
import com.example.evvolicatalogue.data.models.product.ProductEntity

data class CategoryWithProducts(
    @Embedded  val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val products: List<ProductEntity>
)