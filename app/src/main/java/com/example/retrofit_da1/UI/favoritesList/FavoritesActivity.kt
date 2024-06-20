package com.example.retrofit_da1.UI.favoritesList

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit_da1.R
import com.example.retrofit_da1.UI.MainActivity
import com.example.retrofit_da1.UI.ProductDetailActivity
import com.example.retrofit_da1.UI.ProfileActivity
import com.example.retrofit_da1.databinding.ActivityFavoritesBinding
import com.example.retrofit_da1.databinding.FavoriteItemProductBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class FavoritesActivity : AppCompatActivity(), OnFavoriteDeleteListener{
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: favoritesListViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var favoritesAdapter : favoritesListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindView()
        bindViewModel()
        firebaseAuth = FirebaseAuth.getInstance()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_favorites
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    }
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorites -> {
                    val intent = Intent(this, FavoritesActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    }
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart(){
        super.onStart()
        viewModel.onStart()

        viewModel.deleteFavoriteSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Se borró con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun bindView(){
        binding.recyclerProductF.layoutManager = LinearLayoutManager(this)
        favoritesAdapter = favoritesListAdapter(
            onItemClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java).apply {
                    putExtra("productId", product.id)
                }
                startActivity(intent)
            },
            onFavoriteDeleteListener = this
        )
        binding.recyclerProductF.adapter = favoritesAdapter

    }
    private fun bindViewModel(){
        viewModel = ViewModelProvider(this)[favoritesListViewModel::class.java]
        viewModel.favoritesProducts.observe(this){
            favoritesAdapter.update(it)
        }
    }
    override fun onFavoriteDelete(id: Int) {
        viewModel.deleteFavorite(id)
    }
}