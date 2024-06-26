package com.example.retrofit_da1.UI.Main

import android.content.Context
import android.util.Log
import android.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofit_da1.Data.Categories.CategoriesRepository
import com.example.retrofit_da1.Data.FavoriteRepository
import com.example.retrofit_da1.Data.Products.ProductsRepository
import com.example.retrofit_da1.Model.CategorySingle
import com.example.retrofit_da1.Model.FavoriteProduct
import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class MainViewModel: ViewModel() {

    private val coroutineContext: CoroutineContext = newSingleThreadContext("santi")
    private val scope = CoroutineScope(coroutineContext)

    private val ProductRepo = ProductsRepository()
    private  val fr = FavoriteRepository()
    private val categoriesRepository = CategoriesRepository()

    var products = MutableLiveData<MutableList<ProductDetail>>()
    val _productsSearch = MutableLiveData<MutableList<ProductDetail>>()
    var FProducts = MutableLiveData<ArrayList<FavoriteProduct>>()
    var categories = MutableLiveData<MutableList<CategorySingle>>()

    fun onStart(context: Context){
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getAllProducts(context)
            }.onSuccess{
                products.postValue(it)
            }.onFailure {

            }
        }
    }

    fun refresh(context: Context){
        scope.launch {
            try {
                val refreshedProducts = ProductRepo.refresh(context)
                products.postValue(refreshedProducts)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error refreshing products", e)
            }
        }
    }

    fun getCategories(){
        scope.launch {
            kotlin.runCatching {
                categoriesRepository.getAllCategorires()
            }.onSuccess {
                categories.postValue(it)
            }.onFailure {

            }
        }
    }

    /*fun saveFavorite(id:Int,title:String,price:String,images:List<String>){
        scope.launch {
            kotlin.runCatching {
                val fp = FavoriteProduct(id,title,price,images)
                fr.saveFavoriteProduct(fp)
            }.onSuccess {

            }.onFailure {

            }
        }
    }*/

    fun searchProducts(title:String){
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getProductsSearch(title)
            }.onSuccess {
                _productsSearch.postValue(it)
                Log.d("MainViewModel", "Products fetched: $it")
            }.onFailure {
                Log.e("MainViewModel", "Failed to fetch products", it)
            }
        }

    }

    fun getByRangePrice(min:Int,max:Int){
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getProductsByRangePrice(min,max)
            }.onSuccess {
                products.postValue(it)
            }.onFailure {
                Log.e("MainViewModel", "Fallo buscar por rango", it)
            }
        }
    }

    fun getProductsFiltersJoin(min:Int,max:Int,id: Int){
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getProductsFiltersJoin(min,max,id)
            }.onSuccess{
                products.postValue(it)
            }.onFailure {
                Log.e("MainViewModel", "Fallo buscar con filtros", it)
            }
        }
    }

    fun getByCategory(id:Int){
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getProductsByCategory(id)
            }.onSuccess {
                products.postValue(it)
            }.onFailure {
                Log.e("MainViewModel", "Fallo buscar por categoria", it)
            }
        }
    }

    fun isFavorite(id:Int):Boolean{
        return FProducts.value?.any { it.id == id } == true
    }


}
