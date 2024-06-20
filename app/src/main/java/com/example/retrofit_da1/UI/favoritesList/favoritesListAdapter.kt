package com.example.retrofit_da1.UI.favoritesList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit_da1.Model.FavoriteProduct
import com.example.retrofit_da1.Model.ProductDetail
import com.example.retrofit_da1.R

class favoritesListAdapter(
    private val onItemClick: (FavoriteProduct) -> Unit,
    private val onFavoriteDeleteListener: OnFavoriteDeleteListener
) : RecyclerView.Adapter<favoritesListViewHolder>() {
    var products: MutableList<FavoriteProduct> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favoritesListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return favoritesListViewHolder(layoutInflater.inflate(R.layout.favorite_item_product, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: favoritesListViewHolder, position: Int) {
        val item = products[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.findViewById<Button>(R.id.btnEliminar).setOnClickListener {
            onFavoriteDeleteListener.onFavoriteDelete(item.id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: MutableList<FavoriteProduct>) {
        products = list
        this.notifyDataSetChanged()
    }
}
