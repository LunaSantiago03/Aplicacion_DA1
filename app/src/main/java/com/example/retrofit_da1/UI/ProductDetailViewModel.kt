package com.example.retrofit_da1.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofit_da1.Data.ApiException
import com.example.retrofit_da1.Data.ProductsRepository
import com.example.retrofit_da1.Model.ProductDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class ProductDetailViewModel() : ViewModel() {

    private val ProductRepo = ProductsRepository()
    private val coroutineContext: CoroutineContext = newSingleThreadContext("santi")
    private val scope = CoroutineScope(coroutineContext)

    var product = MutableLiveData<ProductDetail>()
    fun loadProductDetail(id: Int) {
        scope.launch {
            kotlin.runCatching {
                ProductRepo.getProductById(id)
            }.onSuccess {
                product.postValue(it)
            }.onFailure {

            }
        }
    }




}
