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
    //private val localDb = AppDataBase.getInstance(context).favoriteProductsDAO()

    suspend fun getFavoritesProducts(context: Context): ArrayList<FavoriteProduct> {
        val localDb = AppDataBase.getInstance(context).favoriteProductsDAO()
        val favoriteProducts = ArrayList<FavoriteProduct>()

        // Consulta los productos favoritos de la base de datos local
        val localFavorites = localDb.getAllFavoriteProducts().toFavoriteProductList()

        if (localFavorites.isNotEmpty()) {
            favoriteProducts.addAll(localFavorites)
            Log.d("FavoriteRepository", "Favorites retrieved from local DB: ${favoriteProducts.size} items")
        } else if (user != null) {
            try {
                val d = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(user.email.toString())
                    .collection("favoritesProducts")
                    .get()
                    .await()

                val remoteFavorites = d.map { document ->
                    document.toObject(FavoriteProduct::class.java)
                }

                // Si la lista remota no está vacía, guarda los productos en la base de datos local
                if (remoteFavorites.isNotEmpty()) {
                    localDb.saveFavoriteProduct(*remoteFavorites.toFavoriteProductListLocal().toTypedArray())
                    favoriteProducts.addAll(remoteFavorites)
                    Log.d("FavoriteRepository", "Favorites retrieved from Firestore: ${remoteFavorites.size} items and saved to local DB")
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
        val localDb = AppDataBase.getInstance(context).favoriteProductsDAO()
        localDb.saveFavoriteProduct(product.toFavoriteProductLocal())
    }

    suspend fun deleteFavoriteProduct(id: String,context: Context): Boolean {
        return try {
            user?.let { user ->
                val userDoc = db.collection("usuarios").document(user.email.toString())
                val favProductDoc = userDoc.collection("favoritesProducts").document(id)
                favProductDoc.delete().await()
                Log.d("ProductsRepository", "Producto eliminado exitosamente")
                val localDb = AppDataBase.getInstance(context).favoriteProductsDAO()
                val productLocal = localDb.getFavoriteProductByID(id.toInt())
                if (productLocal != null) {
                    localDb.deleteFavoriteProduct(productLocal)
                }
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