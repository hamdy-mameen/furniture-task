package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.clickEvent
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.FavoriteItemBinding
import com.example.furnituretask.local.Favorite
import com.example.furnituretask.responses.home.Category
import javax.inject.Inject

class FavoriteAdapter @Inject constructor():RecyclerView.Adapter<FavoriteAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding:FavoriteItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.product_id == newItem.product_id
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
     val category = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(category.image).into(favoriteImg)
            favoriteDetails.text = category.description
            favoriteName.text = category.furniture
            favoritePrice.text = category.price
            root.setOnClickListener {
                clickEvent?.let {
                    it(category.product_id)
                }
            }


        }

    }


    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}