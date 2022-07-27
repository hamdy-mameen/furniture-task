package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.responses.home.Category
import javax.inject.Inject


class CategoryAdapter @Inject constructor():RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding:DiscoverItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
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
                    it(category.category_id,category.name)
                }
            }
        }

    }
    var clickEvent:((Int,String)->Unit)?=null
    fun setListener(event:(Int,String)->Unit){
       clickEvent = event
    }


    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}