package com.example.retrofit_da1.UI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit_da1.Model.ProductDetail
import com.example.retrofit_da1.R

class ProductAdapter(private val onItemClick: (ProductDetail) -> Unit) : RecyclerView.Adapter<ProductViewHolder>() {
    var products: MutableList<ProductDetail> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = products[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: MutableList<ProductDetail>) {
        products = list
        this.notifyDataSetChanged()
    }
}