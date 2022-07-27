package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.responses.furnitureDetails.Menu
import com.example.furnituretask.responses.home.Category
import javax.inject.Inject

class MenuAdapter @Inject constructor():RecyclerView.Adapter<MenuAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding:DiscoverItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = DiscoverItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
     val category = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(category.image).into(discoverItemImg)
            discoverItemName.text = category.name
            root.setOnClickListener{
                clickEvent?.let {
                    it(category.menu_id)
                }
            }
        }

    }
    var clickEvent:((Int)->Unit)?=null
    fun setListener(event:(Int)->Unit){
       clickEvent = event
    }


    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}