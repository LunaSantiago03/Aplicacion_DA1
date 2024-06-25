package com.example.retrofit_da1.Data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.retrofit_da1.Data.LocalDataBase.DataBase.AppDataBase
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toFavoriteProductList
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toFavoriteProductListLocal
import com.example.retrofit_da1.Data.LocalDataBase.Mapping.toFavoriteProductLocal
import com.example.retrofit_da1.Model.FavoriteProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FavoriteRepository() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser


    suspend fun getFavoritesProducts(context: Context): ArrayList<FavoriteProduct> {
        val favoriteProducts = ArrayList<FavoriteProduct>()
        if (user != null) {
            try {
                val d = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(user.email.toString())
                    .collection("favoritesProducts")
                    .get()
                    .await()
                val remoteFavorites = d.map { document ->
                    document.toObject(FavoriteProduct::class.java)
                }
                if (remoteFavorites.isNotEmpty()) {
                    favoriteProducts.addAll(remoteFavorites)
                }
            } catch (e: Exception) {
                Log.e("FavoriteRepository", "Error retrieving favorites from Firestore", e)
            }
        } else {
            Log.d("FavoriteRepository", "User is null")
        }

        return favoriteProducts
    }

    suspend fun saveFavoriteProduct(product: FavoriteProduct, context: Context){
        Log.d("FRViewModel","Guardando favorito")
        if(user != null){
            try {
                FirebaseFirestore.getInstance()
                    .collection("usuarios").document(user.email.toString())
                    .collection("favoritesProducts").document(product.id.toString())
                    .set(product)
                    .await()
                } catch (e:Exception){
                    Log.e("FavoriteRepository", "Error saving to Firestore", e)
                }
        }
    }

    suspend fun deleteFavoriteProduct(id: String,context: Context): Boolean {
        return try {
            user?.let { user ->
                val userDoc = db.collection("usuarios").document(user.email.toString())
                val favProductDoc = userDoc.collection("favoritesProducts").document(id)
                favProductDoc.delete().await()
                Log.d("ProductsRepository", "Producto eliminado exitosamente")
                true
            } ?: run {
                Log.d("ProductsRepository", "Usuario es nulo")
                false
            }
        } catch (e: Exception) {
            Log.w("ProductsRepository", "Error al eliminar producto", e)
            false
        }
    }

}