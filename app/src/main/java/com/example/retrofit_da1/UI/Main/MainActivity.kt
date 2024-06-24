package com.example.retrofit_da1.UI.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofit_da1.Model.CategorySingle
import com.example.retrofit_da1.R
import com.example.retrofit_da1.UI.Profile.AuthViewModel
import com.example.retrofit_da1.UI.Detail.ProductDetailActivity
import com.example.retrofit_da1.UI.Profile.AuthActivity
import com.example.retrofit_da1.UI.components.FiltersDialog
import com.example.retrofit_da1.UI.favoritesList.FavoritesActivity
import com.example.retrofit_da1.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authviewModel: AuthViewModel
    private lateinit var viewModel: MainViewModel
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var pMin: String? = null
    private var pMax: String? = null
    private var selectedCategoryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        authviewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        enableEdgeToEdge()
        setContentView(binding.root)

        if (!isUserLoggedIn()) {
            navigateToAuthActivity()
            return
        }
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home
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
                    signOut()
                    true
                }
                else -> false
            }
        }


        bindView()
        bindViewModel()
        configSwipe()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart(this)
        binding.svProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchProducts(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.onStart(this@MainActivity)
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

    private fun configSwipe(){
        binding.swipe.setOnRefreshListener{
            viewModel.refresh(this)
                binding.swipe.isRefreshing = false

        }
    }
    fun onDialogDismissed() {
        if (!pMin.isNullOrEmpty() && !pMax.isNullOrEmpty()) {
            Toast.makeText(this, "Minimo: $pMin y Maximo: $pMax", Toast.LENGTH_SHORT).show()
            viewModel.getByRangePrice(pMin!!.toInt(),pMax!!.toInt())
        }
        else if(!pMin.isNullOrEmpty() && pMax.isNullOrEmpty()){
            Toast.makeText(this, "Minimo: $pMin", Toast.LENGTH_SHORT).show()
        }
        else if(pMin.isNullOrEmpty() && !pMax.isNullOrEmpty()){
            Toast.makeText(this, "Maximo: $pMax", Toast.LENGTH_SHORT).show()

        }else if(pMin.isNullOrEmpty() && pMax.isNullOrEmpty() && selectedCategoryId != null){
            viewModel.getByCategory(selectedCategoryId!!)
            Toast.makeText(this,"Categoria: $selectedCategoryId",Toast.LENGTH_SHORT).show()
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
            },
            categoriaSeleccionada = { categoryId ->
                selectedCategoryId = categoryId
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

    private fun signOut() {
        authViewModel.signOut()
        clearUserSession()
        navigateToAuthActivity()
    }

    private fun clearUserSession() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.remove("user_id")
        prefs.apply()
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isUserLoggedIn(): Boolean {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        return prefs.getString("user_id", null) != null
    }
}