package com.example.furnituretask.ui.adapters

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.databinding.DiscountsSliderBinding
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.ProductColorBinding
import com.example.furnituretask.databinding.ProductImageItemBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.home.Discount
import com.example.furnituretask.responses.home.Image
import com.example.furnituretask.responses.productbycatId.Color
import javax.inject.Inject

class ProductColorsAdapter @Inject constructor():RecyclerView.Adapter<ProductColorsAdapter.ColorViewHolder>() {
    inner class ColorViewHolder(val binding:ProductColorBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Color>() {
        override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder{
        val binding = ProductColorBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
     val color = differ.currentList[position]
        holder.binding.apply {
            try {
                val gradient =productImageColor.background as GradientDrawable
                val colors =android.graphics.Color.parseColor(color.code)
                gradient.setColor(colors)
            }catch (e:Exception){
                Log.d("TAG","${e.message}")

            }

        }
    }



    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}