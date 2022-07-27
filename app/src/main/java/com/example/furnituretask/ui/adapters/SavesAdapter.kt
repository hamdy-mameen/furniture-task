package com.example.furnituretask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnituretask.clickEvent
import com.example.furnituretask.databinding.DiscoverItemBinding
import com.example.furnituretask.databinding.SavesSliderBinding
import com.example.furnituretask.responses.home.Category
import com.example.furnituretask.responses.home.Offer
import com.example.furnituretask.responses.home.Save
import javax.inject.Inject

class SavesAdapter @Inject constructor():RecyclerView.Adapter<SavesAdapter.SaveViewHolder>() {
    inner class SaveViewHolder(val binding:SavesSliderBinding):RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<Save>() {
        override fun areItemsTheSame(oldItem: Save, newItem: Save): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Save, newItem: Save): Boolean {
            return oldItem ==newItem
        }


    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
        val binding = SavesSliderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SaveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
     val save = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root).load(save.images[0].path).into(savesImg)
            Glide.with(root).load(save.furniture_logo).into(savesSliderFurnitureIcon)
           for (i in 0 until save.products.size){
               when(i){
                   0-> {
                       Glide.with(root).load(save.products[i].icon).into(savesSliderIcon1)
                       savesSliderProduct1.text = save.products[i].product
                   }
                   1-> {
                       Glide.with(root).load(save.products[i].icon).into(savesSliderIcon2)
                       savesSliderProduct2.text = save.products[i].product
                   }

                   2-> {
                       Glide.with(root).load(save.products[i].icon).into(savesSliderIcon3)
                       savesSliderProduct3.text = save.products[i].product
                   }
               }
           }

            savesSliderFurnitureName.text = save.furniture_name
            savesSliderName.text = save.name
            savesSliderPrice.text =  "${save.price}"
            timerDays.text = "${save.diff_day}"
            timerHours.text = "${save.hours}"
            timerMinutes.text = "${save.minutes}"

            root.setOnClickListener {
                clickEvent?.let {
                    it(save.save_id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
      return differ.currentList.size
    }

}