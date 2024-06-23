package com.example.retrofit_da1.Data.LocalDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.retrofit_da1.Data.LocalDataBase.Entities.CategorySingleLocal
import com.example.retrofit_da1.Data.LocalDataBase.Entities.ProductDetailLocal
@Database(
    entities = [ProductDetailLocal::class, CategorySingleLocal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase(){
    abstract fun productsDAO() : ProductsDAO

    companion object{
        @Volatile
        private var _instance : AppDataBase? = null
        fun getInstance(context : Context) : AppDataBase = _instance ?: synchronized(this){
            _instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) : AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java,"app_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}