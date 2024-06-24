package com.example.retrofit_da1.Data.LocalDataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteProducts")
data class FavoriteProductLocal (
    @PrimaryKey val id: Int,
    val title: String,
    val price: String,
    val image: String
)