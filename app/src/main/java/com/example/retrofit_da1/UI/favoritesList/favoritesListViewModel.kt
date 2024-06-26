package com.example.retrofit_da1.UI.favoritesList

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofit_da1.Data.FavoriteRepository
import com.example.retrofit_da1.Model.FavoriteProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class favoritesListViewModel():ViewModel() {
    private val coroutineContext: CoroutineContext = newSingleThreadContext("santi")
    private val scope = CoroutineScope(coroutineContext)
    private  val fr = FavoriteRepository()


    var favoritesProducts = MutableLiveData<ArrayList<FavoriteProduct>>()
    private val _deleteFavoriteSuccess = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val isEmpty = MutableLiveData<Boolean>()
    val deleteFavoriteSuccess: LiveData<Boolean> get() = _deleteFavoriteSuccess

    fun onStart(){
        scope.launch {
            kotlin.runCatching {
                fr.getFavoritesProducts()
            }.onSuccess {
                favoritesProducts.postValue(it)
                isLoading.postValue(false)
                if(it.isEmpty()){
                    isEmpty.postValue(false)
                }
                Log.d("FViewModel", "Favorites fetched successfully: ${it.size} items")
            }.onFailure {
                isLoading.postValue(false)
                Log.e("FViewModel", "Error fetching favorites", it)
            }
        }
    }

    fun deleteFavorite(id: Int){
        scope.launch {
            val success = fr.deleteFavoriteProduct(id.toString())
            _deleteFavoriteSuccess.postValue(success)
            onStart()
        }
    }


}