package com.example.retrofit_da1.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Filter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit_da1.Model.CategorySingle
import com.example.retrofit_da1.R
import com.example.retrofit_da1.UI.components.FiltersDialog
import com.example.retrofit_da1.UI.favoritesList.FavoritesActivity
import com.example.retrofit_da1.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authviewModel: AuthViewModel
    private lateinit var viewModel: MainViewModel
    private lateinit var rvProducts: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var pMin: String? = null
    private var pMax: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        authviewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        enableEdgeToEdge()
        setContentView(binding.root)



        val bundle = intent.extras
        val email = bundle?.getString("email")

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home // Agregado
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
                    Log.d("favo","yendo a favoritos")
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindView()
        bindViewModel()
    }



    override fun onStart() {
        super.onStart()
        viewModel.onStart()
        binding.svProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchProducts(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Cuando el texto está vacío, obtenemos todos los productos
                    viewModel.onStart()
                }
                return false
            }
        })

        viewModel._productsSearch.observe(this, Observer { products ->
            productAdapter.update(products)
        })


        viewModel.products.observe(this, Observer { products ->
            productAdapter.update(products)
        })

    }
    fun onDialogDismissed() {
        if (!pMin.isNullOrEmpty() && !pMax.isNullOrEmpty()) {
            Toast.makeText(this, "Minimo: $pMin y Maximo: $pMax", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showFiltersDialog(categories: List<CategorySingle>) {
        val dialog = FiltersDialog(
            categories,
            precioMinimo = {
                pMin = it
            },
            precioMaximo = {
                pMax = it
            }
        )
        dialog.show(supportFragmentManager, "filtersDialog")
    }

    private fun bindView() {
        binding.recyclerProduct.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("productId", product.id)
            }
            startActivity(intent)
        }
        binding.recyclerProduct.adapter = productAdapter

        binding.btnFilters.setOnClickListener {
            viewModel.getCategories()
        }
    }

    private fun bindViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.products.observe(this) {
            productAdapter.update(it)
        }

        viewModel.categories.observe(this, Observer { categories ->
            showFiltersDialog(categories)
        })
    }

}