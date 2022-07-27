package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.clickEvent
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.OffersSliderBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.home.Discount
import com.example.furnituretask.responses.home.Offer
import javax.inject.Inject

class OffersAdapter @Inject constructor():RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {
    inner class OfferViewHolder(val binding:OffersSliderBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Offer>() {
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = OffersSliderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(offer.images[0].path).into(offersImg)
            Glide.with(root).load(offer.furniture_logo).into(offersSliderFurnitureIcon)
            for (i in 0 until offer.products.size) {
                when (i) {
                    0 -> {
                        Glide.with(root).load(offer.products[i].icon).into(offersSliderIcon1)
                        offersSliderProduct1.text = offer.products[i].product
                    }
                    1 -> {
                        Glide.with(root).load(offer.products[i].icon).into(offersSliderIcon2)
                        offersSliderProduct2.text = offer.products[i].product
                    }

                    2 -> {
                        Glide.with(root).load(offer.products[i].icon).into(offersSliderIcon3)
                        offersSliderProduct3.text = offer.products[i].product
                    }
                }
            }
            offersSliderFurnitureName.text = offer.furniture_name
            offersSliderName.text = offer.name
            offersSliderPrice.text = "${offer.price}"
            root.setOnClickListener {
                clickEvent?.let {
                    it(offer.offer_id)
                }
            }

        }
    }


    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}