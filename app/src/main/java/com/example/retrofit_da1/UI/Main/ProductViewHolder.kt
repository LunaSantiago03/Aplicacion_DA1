package com.example.retrofit_da1.UI.Main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.retrofit_da1.Model.ProductDetail
import com.example.retrofit_da1.databinding.ItemProductBinding

class ProductViewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val binding = ItemProductBinding.bind(view)

    fun bind(product: ProductDetail){
        binding.tvName.text = product.title
        binding.tvPrice.text = "$"+product.price
        val requestOptions = RequestOptions().transform(RoundedCorners(20))
        Glide.with(itemView.context)
            .load(product.images[0])
            .apply(requestOptions)
            .into(binding.ivProduct)
    }
}