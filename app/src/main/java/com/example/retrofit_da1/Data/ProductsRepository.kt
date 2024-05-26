package com.example.retrofit_da1.Data

import com.example.retrofit_da1.Model.ProductDetail

class ProductsRepository {

    private val PS = ProductService()

    suspend fun getAllProducts(): MutableList<ProductDetail> {
        return PS.getProducts()
    }

    suspend fun getProductById(id: Int): ProductDetail {
        return PS.getProductById(id)
    }

}