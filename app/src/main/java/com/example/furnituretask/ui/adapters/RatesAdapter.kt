package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.RatesItemBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.productDetails.Rate
import javax.inject.Inject

class RatesAdapter @Inject constructor():RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {
    inner class RateViewHolder(val binding:RatesItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Rate>() {
        override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val binding = RatesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
     val rate = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(rate.avatar).apply(RequestOptions.circleCropTransform()).into(ratesItemImg)
            ratesItemName.text = rate.user
            ratesItemRating.rating = rate.degree.toFloat()
            rateComment.text = rate.comment
            rateItemDate.text = rate.date

        }

    }

    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}