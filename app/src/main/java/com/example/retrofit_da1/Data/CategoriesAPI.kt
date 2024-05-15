package com.example.retrofit_da1.Data

import com.example.retrofit_da1.Model.CategorySingle
import com.example.retrofit_da1.Model.ProductDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.IDN

interface CategoriesAPI {
    @GET("categories")
    suspend fun getAllCategories():
            Response<List<CategorySingle>>

    @GET("categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Int
        ):
        Response<CategorySingle>

    @GET("categories/{ID}/products")
    suspend fun getProductsById(
        @Path("ID") id: Int
        ):
        Response<List<ProductDetail>>
}