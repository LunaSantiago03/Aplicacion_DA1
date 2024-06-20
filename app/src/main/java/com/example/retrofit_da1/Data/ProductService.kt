package com.example.retrofit_da1.Data

import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.time.delay
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

    suspend fun getProductById(id: Int): ProductDetail {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ProductsAPI::class.java).getProductById(id)
            if (response.isSuccessful) {
                response.body() ?: throw ApiException("Product not found")
            } else {
                throw ApiException("Failed to fetch product: ${response.code()} - ${response.message()}")
            }
        }
    }

    suspend fun getProductsSearch(title: String): MutableList<ProductDetail> {
        return withContext(Dispatchers.IO) {
            val res = retrofit.create(ProductsAPI::class.java).getProductsSearch(title)
            if (res.isSuccessful) {
                res.body()?.toMutableList() ?: throw ApiException("Product not found")
            } else {
                throw ApiException("Failed to fetch product: ${res.code()} - ${res.message()}")
            }
        }
    }



}
class ApiException(message: String) : Exception(message)
