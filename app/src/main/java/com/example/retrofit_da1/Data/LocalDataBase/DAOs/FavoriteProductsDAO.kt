package com.example.retrofit_da1.Data.LocalDataBase.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.retrofit_da1.Data.LocalDataBase.Entities.FavoriteProductLocal

@Dao
interface FavoriteProductsDAO{
    @Query("SELECT * FROM FavoriteProducts")
    fun getAllFavoriteProducts(): List<FavoriteProductLocal>

    @Query("SELECT * FROM FavoriteProducts WHERE id = :id")
    fun getFavoriteProductByID(id: Int): FavoriteProductLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFavoriteProduct(vararg product: FavoriteProductLocal)

    @Delete
    fun deleteFavoriteProduct(product: FavoriteProductLocal)
}