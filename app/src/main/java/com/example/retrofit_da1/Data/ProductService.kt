package com.example.retrofit_da1.Data

import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getProducts(): MutableList<ProductDetail> {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(ProductsAPI::class.java).getAllProducts()
            if(response.isSuccessful){
                response.body() ?: emptyList()
            }else{
                throw ApiException("Failed to fetch products: ${response.code()} - ${response.message()}")
            }
        }.toMutableList()
    }



}
class ApiException(message: String) : Exception(message)