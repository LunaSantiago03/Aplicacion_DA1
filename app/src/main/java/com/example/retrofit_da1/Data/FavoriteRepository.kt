package com.example.retrofit_da1.Data

import android.util.Log
import android.widget.Toast
import com.example.retrofit_da1.Model.FavoriteProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FavoriteRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    suspend fun getFavoritesProducts():ArrayList<FavoriteProduct>{
        val fp = ArrayList<FavoriteProduct>()
        if(user!= null){
            try {
                val d = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(user.email.toString())
                    .collection("favoritesProducts")
                    .get()
                    .await()
                for(document in d){
                    val pro = document.toObject(FavoriteProduct::class.java)
                    fp.add(pro)
                }
                Log.d("FRViewModel", "Favorites retrieved: ${fp.size} items")
            }catch (e:Exception){
                Log.e("FRViewModel", "Error al recuperar", e)
            }
        }else{
            Log.d("FRViewModel", "user es null")
        }
        return fp
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
        }else{

        }
    }

    suspend fun deleteFavoriteProduct(id: String): Boolean {
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