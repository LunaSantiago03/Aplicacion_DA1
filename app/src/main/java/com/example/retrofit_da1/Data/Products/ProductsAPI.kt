package com.example.retrofit_da1.Data.Products

import com.example.retrofit_da1.Model.ProductDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsAPI {
    @GET("products")
    suspend fun getAllProducts():
            Response<List<ProductDetail>>

    @GET("products/{id}")
    suspend fun getProductById(
            @Path("id") id: Int):
            Response<ProductDetail>

    @GET("products/price={price}")
    suspend fun getProductByPrice(
        @Path("price") price: Int
        ):
        Response<List<ProductDetail>>
    @GET("products/?price_min={priceMin}&price_max={priceMax}")
    suspend fun getProductsByPriceRange(
        @Path("priceMin") priceMin: Int,
        @Path("priceMax") priceMax: Int
        ):
        Response<List<ProductDetail>>
    @GET("products/?categoryId={id}")
    suspend fun getProductsByCategory(
        @Path("id") id: Int
        ):
        Response<List<ProductDetail>>

    /*@GET("products/?title={title}")
    suspend fun getProductsSearch(
        @Query("title") title:String
    ):
    Response<List<ProductDetail>>*/
    @GET("products")
    suspend fun getProductsSearch(
        @Query("title") title: String
    ): Response<List<ProductDetail>>
}