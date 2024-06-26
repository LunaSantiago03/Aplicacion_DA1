package com.example.retrofit_da1.UI.Detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.retrofit_da1.R
import com.example.retrofit_da1.UI.favoritesList.favoritesListViewModel
import com.example.retrofit_da1.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var viewModelF: favoritesListViewModel



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val productId = intent.getIntExtra("productId", -1)
        bindViewModel(productId)


        viewModel.FProducts.observe(this) { favorites ->
            val productId = intent.getIntExtra("productId", -1)
            Log.d("DetailActivity", "Favorites loaded: ${favorites.size}")
            if (viewModel.isFavorite(productId)) {
                binding.btnSaveFavorite.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.save, 0, 0)
            } else {
                binding.btnSaveFavorite.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nosave, 0, 0)
            }
        }
        viewModel.product.observe(this) { product ->
            Glide.with(binding.root.context)
                .load(product.images[0])
                .into(binding.ivProduct)
            binding.tvTitle.text = product.title
            binding.tvPrice.text = "$"+product.price
            binding.tvDescription.text = product.description
            binding.tvCategory.text = "Category: "+product.category.name
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvDescripcionTitulo.visibility = View.VISIBLE
            binding.btnSaveFavorite.visibility = View.VISIBLE
        }



        observe(productId)
    }

    override fun onStart() {
        super.onStart()
        val productId = intent.getIntExtra("productId", -1)
        binding.tvDescripcionTitulo.visibility = View.INVISIBLE
        binding.btnSaveFavorite.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadFavorites()
        viewModel.loadProductDetail(productId,this)



    }

    private fun bindViewModel(productId : Int) {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(ProductDetailViewModel::class.java)
        //viewModel.loadProductDetail(productId,this)
    }

    private fun observe(productID : Int){
        binding.btnSaveFavorite.setOnClickListener{
            if(viewModel.isFavorite(productID)){
                viewModel.deleteFavorite(productID)
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                binding.btnSaveFavorite.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nosave, 0, 0)
            }else{
                viewModel.saveFavorite()
                Toast.makeText(this,"Guardado con exito",Toast.LENGTH_SHORT).show()
                binding.btnSaveFavorite.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.save, 0, 0)
            }
        }
        binding.btnBack.setOnClickListener{
            finish()
        }
    }


}
