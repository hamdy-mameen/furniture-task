package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.clickEvent
import com.example.furnituretask.databinding.DiscountsSliderBinding
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.home.Discount
import javax.inject.Inject

class DiscountsAdapter @Inject constructor():RecyclerView.Adapter<DiscountsAdapter.DiscountViewHolder>() {
    inner class DiscountViewHolder(val binding:DiscountsSliderBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Discount>() {
        override fun areItemsTheSame(oldItem: Discount, newItem: Discount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Discount, newItem: Discount): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        val binding = DiscountsSliderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DiscountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
     val discount = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(discount.images[0].path).into(discountsImg)
            Glide.with(root).load(discount.furniture_logo).into(discountsSliderFurnitureIcon)
            discountsSliderName.text = discount.product_name
            discountsDetails.text = discount.product_description
            discountsSliderFurnitureName.text = discount.furniture_name
            currPrice.text = "${discount.price_after}"
            prevPrice.text = "${discount.price_before}"
            root.setOnClickListener {
                clickEvent?.let {
                    it(discount.discount_id)
                }
            }

        }
    }



    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}