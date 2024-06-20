package com.example.retrofit_da1.UI

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.example.retrofit_da1.Data.FavoriteRepository
import com.example.retrofit_da1.R
import com.example.retrofit_da1.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var context: Context



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val productId = intent.getIntExtra("productId", -1)
        bindViewModel(productId)

        viewModel.product.observe(this) { product ->
            Glide.with(binding.root.context)
                .load(product.images[0])
                .into(binding.ivProduct)
            binding.tvTitle.text = product.title
            binding.tvPrice.text = "$"+product.price
            binding.tvDescription.text = product.description
            binding.tvCategory.text = "Category: "+product.category.name
            binding.progressBar.visibility = View.INVISIBLE
        }
        observe(productId)
    }

    override fun onStart() {
        super.onStart()
        val productId = intent.getIntExtra("productId", -1)
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadProductDetail(productId)

    }

    private fun bindViewModel(productId : Int) {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(ProductDetailViewModel::class.java)
        viewModel.loadProductDetail(productId)
    }

    private fun observe(productID : Int){
        binding.btnSaveFavorite.setOnClickListener{
            if(viewModel.isFavorite(productID)){
                viewModel.deleteFavorite()
            }else{
                viewModel.saveFavorite()
            }
        }
    }
}
