package com.example.retrofit_da1.Data.LocalDataBase.Entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithCategory  (
    @Embedded val product: ProductDetailLocal,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategorySingleLocal
)