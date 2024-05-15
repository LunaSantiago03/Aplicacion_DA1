package com.example.retrofit_da1.UI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofit_da1.Data.ProductsRepository
import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MainViewModel: ViewModel() {

    private val coroutineContext: CoroutineContext = newSingleThreadContext("santi")
    private val scope = CoroutineScope(coroutineContext)

    private val ProductRepo = ProductsRepository()

    var products = MutableLiveData<MutableList<ProductDetail>>()
    var product = MutableLiveData<ProductDetail>()

    fun onStart(){
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getAllProducts()
            }.onSuccess{
                products.postValue(it)
            }.onFailure {

            }
        }
    }


}
