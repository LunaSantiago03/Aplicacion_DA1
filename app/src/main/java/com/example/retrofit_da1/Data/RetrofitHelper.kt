package com.example.retrofit_da1.Data

import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    fun getRetrofit():Retrofit{
        return Retrofit.Builder().
                baseUrl("https://api.escuelajs.co/api/v1/").
                addConverterFactory(GsonConverterFactory.create()).
                build()
    }
}