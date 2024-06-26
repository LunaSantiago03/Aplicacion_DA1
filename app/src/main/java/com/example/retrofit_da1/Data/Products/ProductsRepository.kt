package com.example.retrofit_da1.Data.Products

import android.content.Context
import com.example.retrofit_da1.Data.Products.ProductService
import com.example.retrofit_da1.Model.ProductDetail

class ProductsRepository {

    private val PS = ProductService()

    suspend fun getAllProducts(context: Context): MutableList<ProductDetail> {
        return PS.getProducts(context)
    }
    suspend fun refresh(context: Context):MutableList<ProductDetail>{
        return PS.refresh(context)
    }

    suspend fun getProductById(id: Int,context: Context): ProductDetail {
        return PS.getProductById(id,context)
    }

    suspend fun getProductsSearch(title:String) :MutableList<ProductDetail>{
        return PS.getProductsSearch(title)
    }

    suspend fun getProductsByRangePrice(min: Int, max: Int):MutableList<ProductDetail>{
        return PS.getProductsByPriceRange(min,max)
    }

    suspend fun getProductsByCategory(id: Int):MutableList<ProductDetail>{
        return PS.getProductsByCategory(id)
    }

    suspend fun getProductsFiltersJoin(min: Int, max: Int,id: Int) : MutableList<ProductDetail>{
        return PS.getProductsFiltersJoin(min, max, id)
    }


}