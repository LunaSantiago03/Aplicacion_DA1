package com.example.retrofit_da1.Data.LocalDataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Categories")
data class CategorySingleLocal(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String
)