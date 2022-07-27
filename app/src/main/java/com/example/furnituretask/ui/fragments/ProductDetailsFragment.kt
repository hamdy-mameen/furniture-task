package com.example.furnituretask.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentProductDetailsBinding
import com.example.furnituretask.local.Favorite
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.*
import com.example.furnituretask.viewModels.ProductByIdViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    private var binding:FragmentProductDetailsBinding?=null
    private val viewModel:ProductByIdViewModel by viewModels()
    @Inject
     lateinit var ratesAdapter: RatesAdapter
    @Inject
    lateinit var productImagesAdapter: ProductImagesAdapter
    @Inject
    lateinit var colorsAdapter: ProductColorsAdapter
    private val args:ProductDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRateRecyclerView()
        setupColorsRecyclerView()
        setupPagerAdapter()
        viewModel.getProductDetails(args.id)
        viewModel.productDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                    ratesAdapter.differ.submitList(resources.data?.data?.rates)
                    productImagesAdapter.differ.submitList(resources.data?.data?.product?.images)
                    colorsAdapter.differ.submitList(resources.data?.data?.product?.colors)
                    resources.data?.data?.product?.let{ product->
                        binding?.let {
                            it.productDetailsName.text = product.name
                            it.productDetailsRating.rating = product.rate.toFloat()
                            it.productDetailsDescription.text = product.description
                            it.productPrice.text = getString(R.string.price_txt) + "${product.price}"
                            it.productDetailsWishlist.setOnClickListener {
                                val favorite = Favorite(product.product_id,product.images[0].path,product.name,product.description,product.price.toString())
                                viewModel.insertFavorite(favorite)
                                Toast.makeText(requireContext(), getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                            }

                        }
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
    private fun setupRateRecyclerView(){
        binding?.let {
            it.productDetailsRecyclerView  .apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ratesAdapter
            }
        }
    }
    private fun setupColorsRecyclerView(){
        binding?.let {
            it.colorsList.apply{
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
                adapter = colorsAdapter
            }
        }
    }
    private fun setupPagerAdapter(){
        binding?.let {
            it.detailsPager.apply {
                adapter = productImagesAdapter
                it.tabLayout.apply {
                    TabLayoutMediator(this,it.detailsPager){tab,position->

                    }.attach()
                }
                }


            }

        }

    
}