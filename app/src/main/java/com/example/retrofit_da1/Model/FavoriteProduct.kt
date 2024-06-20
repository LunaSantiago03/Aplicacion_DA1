package com.example.retrofit_da1.Model

import com.google.gson.annotations.SerializedName

data class FavoriteProduct (
    val id: Int = 0,
    val title: String = "",
    val price: String = "",
    val images: List<String> = listOf()
) {
    // Constructor vacío requerido por Firestore
    constructor() : this(0, "", "", listOf())
}

