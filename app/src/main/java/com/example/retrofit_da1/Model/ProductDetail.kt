package com.example.retrofit_da1.Model

data class ProductDetail (
    val id: Int,
    val title: String,
    val price: String,
    val description: String,
    val category: CategorySingle,
    val images: List<String>
)
