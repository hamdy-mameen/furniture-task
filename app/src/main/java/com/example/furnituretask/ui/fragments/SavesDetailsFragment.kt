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
import com.example.furnituretask.viewModels.MainViewModel
import com.example.furnituretask.viewModels.SaveDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavesDetailsFragment: Fragment() {
    private var binding:FragmentOfferDetailsBinding?=null
    private val viewModel:SaveDetailsViewModel by viewModels()
    private val args:SavesDetailsFragmentArgs by navArgs()
    @Inject
     lateinit var imageAdapter:ProductImagesAdapter
    @Inject
     lateinit var productAdapter:OffersProductsAdapter
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
        setupRecyclerView()
        setupPagerAdapter()
        viewModel.getSaveDetails(args.id)
        viewModel.savesDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                    resources.data?.data?.let{ offer->
                   binding?.let {
                       it.productEnddate.visibility = View.VISIBLE
                       it.productDetailsName.text = offer.name
                       it.productDetailsRating.rating = offer.rate.toFloat()
                       it.productPrice.text =getString(R.string.price_txt) + " ${offer.price}"
                       it.productFurniture.text = getString(R.string.offer_available) +"${offer.furniture_name} "
                       it.productEnddate.text = getString(R.string.offer_ended)+ "${offer.end} "

                   }
                        imageAdapter.differ.submitList(offer.images)
                        productAdapter.differ.submitList(offer.products)



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
    private fun setupRecyclerView(){
        binding?.let {
            it.productDetailsRecyclerView  .apply{
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
                adapter = productAdapter
            }
        }
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