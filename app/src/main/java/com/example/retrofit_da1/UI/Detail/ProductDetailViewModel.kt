package com.example.retrofit_da1.UI.Detail

import android.content.Context
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
    fun isFavorite(id:Int):Boolean{
        return FProducts.value?.any { it.id == id } == true
    }

    fun saveFavorite(context:Context){
        scope.launch {
            kotlin.runCatching {
                fr.saveFavoriteProduct(favoriteProduct,context)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun deleteFavorite(){

    }




}
