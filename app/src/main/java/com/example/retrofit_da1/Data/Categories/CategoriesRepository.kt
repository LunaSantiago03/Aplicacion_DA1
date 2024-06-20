package com.example.retrofit_da1.Data.Categories

import com.example.retrofit_da1.Model.CategorySingle

class CategoriesRepository {

    private val CS = CategoriesService()

    suspend fun getAllCategorires() : MutableList<CategorySingle>{
        return CS.getCategories()
    }
}