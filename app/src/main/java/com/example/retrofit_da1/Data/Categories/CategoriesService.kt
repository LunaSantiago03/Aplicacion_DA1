package com.example.retrofit_da1.Data.Categories

import com.example.retrofit_da1.Data.Products.ApiException
import com.example.retrofit_da1.Data.RetrofitHelper
import com.example.retrofit_da1.Model.CategorySingle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getCategories(): MutableList<CategorySingle>{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(CategoriesAPI::class.java).getAllCategories()
            if(response.isSuccessful){
                response.body() ?: emptyList()
            }else{
                throw ApiException("Failed to fetch products: ${response.code()} - ${response.message()}")
            }
        }.toMutableList()
    }
}