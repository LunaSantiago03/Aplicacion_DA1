package com.example.retrofit_da1.UI.Detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofit_da1.Data.FavoriteRepository
import com.example.retrofit_da1.Data.Products.ProductsRepository
import com.example.retrofit_da1.Model.FavoriteProduct
import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class ProductDetailViewModel() : ViewModel() {

    private val ProductRepo = ProductsRepository()
    private val coroutineContext: CoroutineContext = newSingleThreadContext("product")
    private val scope = CoroutineScope(coroutineContext)
    private  val fr = FavoriteRepository()

    var product = MutableLiveData<ProductDetail>()
    var FProducts = MutableLiveData<ArrayList<FavoriteProduct>>()
    lateinit var favoriteProduct: FavoriteProduct


    fun loadProductDetail(id: Int,context: Context) {
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getProductById(id,context)
            }.onSuccess {
                product.postValue(it ?: ProductDetail())
                favoriteProduct = FavoriteProduct(it.id,it.title,it.price,it.images)
            }.onFailure {
                product.postValue(ProductDetail())
            }
        }
    }

    fun loadFavorites(){
        scope.launch {
            kotlin.runCatching {
                fr.getFavoritesProducts()
            }.onSuccess {
                FProducts.postValue(it)
                Log.d("FViewModel", "Favorites fetched successfully: ${it.size} items")
            }.onFailure {
                Log.e("FViewModel", "Error fetching favorites", it)
            }
        }
    }
    fun isFavorite(id:Int):Boolean{
        return FProducts.value?.any { it.id == id } == true
    }

    fun saveFavorite() {
        scope.launch {
            kotlin.runCatching {
                fr.saveFavoriteProduct(favoriteProduct)
            }.onSuccess {
                loadFavorites() // Refrescar la lista de favoritos después de guardar
            }.onFailure {
                Log.e("FViewModel", "Error saving favorite", it)
            }
        }
    }
    fun deleteFavorite(id:Int) {
        scope.launch {
            kotlin.runCatching {
                fr.deleteFavoriteProduct(id.toString())
            }.onSuccess {
                loadFavorites() // Refrescar la lista de favoritos después de eliminar
            }.onFailure {
                Log.e("FViewModel", "Error deleting favorite", it)
            }
        }
    }




}
