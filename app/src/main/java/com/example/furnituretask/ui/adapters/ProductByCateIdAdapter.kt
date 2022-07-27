package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.R
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.ProductItemBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.productbycatId.Product
import javax.inject.Inject

class ProductByCateIdAdapter @Inject constructor():RecyclerView.Adapter<ProductByCateIdAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding:ProductItemBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
     val product = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(product.images[0].path).into(discountsImg)
            Glide.with(root).load(product.furniture.logo).into(discountsSliderFurnitureIcon)
            productItemRating.rating = product.rate.toFloat()
            discountsSliderName.text = product.name
            productItemRate.text = product.rate_count.toString()
            discountsDetails.text = product.description
            discountsSliderFurnitureName.text = product.furniture.name
            currPrice.text = "${product.price}"
            root.setOnClickListener{
                clickEvent?.let {
                    it(product.product_id)
                }
            }
            productItemWishlist.setOnClickListener {
                favoriteListener?.let {
                    it(product)
                }
                productItemWishlist.setImageResource(R.drawable.ic_favorite_)
            }
        }

    }
    var clickEvent:((Int)->Unit)?=null

    fun setListener(event:(Int)->Unit){
       clickEvent = event
    }


    var favoriteListener:((Product)->Unit)?=null
    fun setClickListener(click:(Product)->Unit){
        favoriteListener = click
    }


    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}