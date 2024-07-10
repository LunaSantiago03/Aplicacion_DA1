package com.example.retrofit_da1.Data


import android.util.Log
import com.example.retrofit_da1.Model.FavoriteProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoriteRepository() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser


    suspend fun getFavoritesProducts(): ArrayList<FavoriteProduct> {
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

    suspend fun saveFavoriteProduct(product: FavoriteProduct){
        Log.d("FRViewModel","Guardando favorito")
        if(user != null){
            try {
                FirebaseFirestore.getInstance()
                    .collection("usuarios").document(user.email.toString())
                    .collection("favoritesProducts").document(product.id.toString())
                    .set(product)
                    .await()
                } catch (e:Exception){
                }
        }
    }

    suspend fun deleteFavoriteProduct(id: String): Boolean {
        return try {
            user?.let { user ->
                val userDoc = db.collection("usuarios").document(user.email.toString())
                val favProductDoc = userDoc.collection("favoritesProducts").document(id)
                favProductDoc.delete().await()
                true
            } ?: run {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

}