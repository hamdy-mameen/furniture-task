package com.example.furnituretask.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentOfferDetailsBinding
import com.example.furnituretask.databinding.FragmentProductDetailsBinding
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.*
import com.example.furnituretask.viewModels.DiscountDetailsViewModel
import com.example.furnituretask.viewModels.DiscountsViewModel
import com.example.furnituretask.viewModels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscountsDetailsFragment: Fragment() {
    private var binding:FragmentOfferDetailsBinding?=null
    private val viewModel:DiscountDetailsViewModel by viewModels()
    private val args:DiscountsDetailsFragmentArgs by navArgs()
    @Inject
    lateinit var imageAdapter:ProductImagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfferDetailsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPagerAdapter()
        viewModel.getDiscountDetails(args.id)
        viewModel.discountDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                    resources.data?.data?.let{ offer->
                   binding?.let {
                       it.productEnddate.visibility = View.VISIBLE
                       it.productDetailsRecyclerView.visibility = View.GONE
                       it.productDetailsName.text = offer.product_name
                       it.productDetailsRating.rating = offer.rate.toFloat()
                       val before=  getString(R.string.price_before,offer.price_before)
                       val after=  getString(R.string.price_after,offer.price_after)
                       it.productPrice.text = before
                       it.productEnddate.text =after
                       it.productDetailsDetails.textSize = 16F
                       it.productDetailsDetails.text = getString(R.string.details)+"${offer.product_description}"

                   }
                        imageAdapter.differ.submitList(offer.images)




                    }

                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
            }

        })
        
    }


    private fun setupPagerAdapter(){
        binding?.let {
            it.detailsPager.apply {
                adapter = imageAdapter
                it.tabLayout.apply {
                    TabLayoutMediator(this,it.detailsPager){tab,position->

                    }.attach()
                }
                }


            }

        }

    
}