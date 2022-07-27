package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.NearByHomeBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.nearByFurnitures.Data
import javax.inject.Inject

class NearByHomeAdapter @Inject constructor():RecyclerView.Adapter<NearByHomeAdapter.NearHomeViewHolder>() {
    inner class NearHomeViewHolder(val binding:NearByHomeBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearHomeViewHolder {
        val binding = NearByHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NearHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearHomeViewHolder, position: Int) {
     val data = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(data.logo).apply(RequestOptions.circleCropTransform()).into(nearByHomeImg)
            nearByHomeName.text = data.name
        }

    }


    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}