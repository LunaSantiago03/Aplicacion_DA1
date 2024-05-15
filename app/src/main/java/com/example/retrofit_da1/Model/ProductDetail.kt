package com.example.retrofit_da1.Model

import com.google.gson.annotations.SerializedName

data class ProductDetail (
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: CategorySingle,
    @SerializedName("images") val images: List<String>
)
