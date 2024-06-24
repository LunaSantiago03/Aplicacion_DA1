package com.example.retrofit_da1.Data.Products

import android.content.Context
import com.example.retrofit_da1.Data.LocalDataBase.DataBase.AppDataBase
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toCategorySingleLocal
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toProduct
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toProductList
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toProductListLocal
import com.example.retrofit_da1.Data.RetrofitHelper
import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductService () {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getProducts(context: Context): MutableList<ProductDetail> {
        val db = AppDataBase.getInstance(context)
        val productsLocal = db.productsDAO().getAllProductsWithCategory()
        if (productsLocal.isNotEmpty()) {
            return productsLocal.toProductList() as MutableList<ProductDetail>
        }

        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ProductsAPI::class.java).getAllProducts()
            if (response.isSuccessful) {
                val productList = response.body() ?: emptyList()
                if (productList.isNotEmpty()) {
                    // Guardar categorías primero
                    val categories = productList.map { it.category }.distinctBy { it.id }
                    db.productsDAO().saveCategory(*categories.map { it.toCategorySingleLocal() }.toTypedArray())

                    // Guardar productos
                    db.productsDAO().saveProduct(*productList.toProductListLocal().toTypedArray())
                }
                productList
            } else {
                throw ApiException("Failed to fetch products: ${response.code()} - ${response.message()}")
            }
        }.toMutableList()
    }

    suspend fun refresh(context: Context): MutableList<ProductDetail>{
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ProductsAPI::class.java).getAllProducts()
            if (response.isSuccessful) {
                val productList = response.body() ?: emptyList()
                if (productList.isNotEmpty()) {
                    // Guardar categorías primero
                    val db = AppDataBase.getInstance(context)
                    val categories = productList.map { it.category }.distinctBy { it.id }
                    db.productsDAO().saveCategory(*categories.map { it.toCategorySingleLocal() }.toTypedArray())

                    // Guardar productos
                    db.productsDAO().saveProduct(*productList.toProductListLocal().toTypedArray())
                }
                productList
            } else {
                throw ApiException("Failed to fetch products: ${response.code()} - ${response.message()}")
            }
        }.toMutableList()
    }


    suspend fun getProductById(id: Int, context: Context): ProductDetail {
        val db = AppDataBase.getInstance(context)
        val product = db.productsDAO().getProductWithCategoryByID(id)
        if(product != null){
            return product.toProduct()
        }

        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ProductsAPI::class.java).getProductById(id)
            if (response.isSuccessful) {
                response.body() ?: throw ApiException("Product not found")
            } else {
                throw ApiException("Failed to fetch product: ${response.code()} - ${response.message()}")
            }
        }
    }

    suspend fun getProductsSearch(title: String): MutableList<ProductDetail> {
        return withContext(Dispatchers.IO) {
            val res = retrofit.create(ProductsAPI::class.java).getProductsSearch(title)
            if (res.isSuccessful) {
                res.body()?.toMutableList() ?: throw ApiException("Product not found")
            } else {
                throw ApiException("Failed to fetch product: ${res.code()} - ${res.message()}")
            }
        }
    }

    suspend fun getProductsByPriceRange(min: Int, max: Int):MutableList<ProductDetail>{
        return withContext(Dispatchers.IO){
            val res = retrofit.create(ProductsAPI::class.java).getProductsByPriceRange(min,max)
            if (res.isSuccessful) {
                res.body()?.toMutableList() ?: throw ApiException("Product not found")
            } else {
                throw ApiException("Failed to fetch product: ${res.code()} - ${res.message()}")
            }
        }
    }

    suspend fun getProductsByCategory(id: Int) : MutableList<ProductDetail>{
        return withContext(Dispatchers.IO){
            val res = retrofit.create(ProductsAPI::class.java).getProductsByCategory(id)
            if(res.isSuccessful){
                res.body()?.toMutableList() ?: throw ApiException("Products not found")
            }else{
                throw ApiException("Failed to fetch product: ${res.code()} - ${res.message()}")
            }
        }
    }



}class ApiException(message: String) : Exception(message)
