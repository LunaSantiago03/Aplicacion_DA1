package com.example.retrofit_da1.Data.LocalDataBase.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.retrofit_da1.Data.LocalDataBase.Entities.CategorySingleLocal
import com.example.retrofit_da1.Data.LocalDataBase.Entities.ProductDetailLocal
import com.example.retrofit_da1.Data.LocalDataBase.Entities.ProductWithCategory

@Dao
interface ProductsDAO {
    @Transaction
    @Query("SELECT * FROM Products")
    fun getAllProductsWithCategory(): List<ProductWithCategory>

    @Transaction
    @Query("SELECT * FROM Products WHERE id = :id")
    fun getProductWithCategoryByID(id: Int): ProductWithCategory


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(vararg product: ProductDetailLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCategory(vararg category: CategorySingleLocal)
    @Delete
    fun deleteProduct(product: ProductDetailLocal)

    @Query("DELETE FROM Products")
    fun clearAllProducts()

    @Query("DELETE FROM Categories")
    fun clearAllCategories()
}