package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.responses.home.Product
import javax.inject.Inject


class OffersProductsAdapter @Inject constructor():RecyclerView.Adapter<OffersProductsAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding:DiscoverItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.product_id == newItem.product_id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = DiscoverItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
     val product = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(product.icon).into(discoverItemImg)
           discoverItemName.text = product.product
            }
        }





    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}