package com.example.retrofit_da1.UI.favoritesList

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.retrofit_da1.Model.FavoriteProduct
import com.example.retrofit_da1.Model.ProductDetail
import com.example.retrofit_da1.R
import com.example.retrofit_da1.databinding.ActivityFavoritesBinding
import com.example.retrofit_da1.databinding.FavoriteItemProductBinding
import com.example.retrofit_da1.databinding.ItemProductBinding

class favoritesListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = FavoriteItemProductBinding.bind(view)

    fun bind(product: FavoriteProduct) {
        binding.tvTitle.text = product.title
        binding.tvPrice.text = "$" + product.price

        val requestOptions = RequestOptions().transform(RoundedCorners(20))
        Glide.with(itemView.context)
            .load(product.images[0])
            .apply(requestOptions)
            .into(binding.ivFoto)
    }
}
