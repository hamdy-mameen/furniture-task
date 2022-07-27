package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.databinding.DiscountsSliderBinding
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.ProductImageItemBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.home.Discount
import com.example.furnituretask.responses.home.Image
import javax.inject.Inject

class ProductImagesAdapter @Inject constructor():RecyclerView.Adapter<ProductImagesAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(val binding:ProductImageItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder{
        val binding = ProductImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
     val image = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(image.path).into(productImageItemimg)
        }
    }



    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}