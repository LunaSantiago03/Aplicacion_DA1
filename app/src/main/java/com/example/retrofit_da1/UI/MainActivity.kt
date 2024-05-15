package com.example.retrofit_da1.UI

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit_da1.Data.ProductsRepository
import com.example.retrofit_da1.R
import com.example.retrofit_da1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var rvProducts: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        //initRecyclerView()
        bindView()
        bindViewModel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun bindView(){
        /*rvProducts = findViewById(R.id.recyclerProduct)
        rvProducts.layoutManager = GridLayoutManager(this,2)
        productAdapter = ProductAdapter()
        rvProducts.adapter = productAdapter*/
        binding.recyclerProduct.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter()
        binding.recyclerProduct.adapter = productAdapter

    }

    private fun bindViewModel(){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.products.observe(this){
            productAdapter.update(it)
        }

    }

    /*
    * private fun initRecyclerView(){
        binding.recyclerProduct.layoutManager = GridLayoutManager(this,2)
        binding.recyclerProduct.adapter = ProductAdapter(ProductsRepository().getAllProducts())
    }*/
}