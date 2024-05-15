package com.example.retrofit_da1.Data

import com.example.retrofit_da1.Model.ProductDetail

class ProductsRepository {

    private val PR = ProductService()

    suspend fun getAllProducts(): MutableList<ProductDetail> {
        return PR.getProducts()
    }

}