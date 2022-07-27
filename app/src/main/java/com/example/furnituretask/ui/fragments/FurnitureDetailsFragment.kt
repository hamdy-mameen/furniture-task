package com.example.furnituretask.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentBranchesBinding
import com.example.furnituretask.databinding.FragmentFurnitureBinding
import com.example.furnituretask.databinding.FragmentFurniturePageBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.ViewPagerAdapter
import com.example.furnituretask.viewModels.FurnitureViewModel
import com.example.furnituretask.viewModels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class FurnitureDetailsFragment: Fragment() {
    private var _binding:FragmentFurnitureBinding?=null
    private val binding get() = _binding!!
    private val viewModel:FurnitureViewModel by activityViewModels()
    private val args:FurnitureDetailsFragmentArgs by navArgs()
val tabArray = arrayOf("معلومات عن المعرض","التقييم","صفحة المعرض")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFurnitureBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFurnitureDetails(args.id)

        viewModel.furnitureDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources) {
                is Resources.Loading -> Toast.makeText(requireContext(),getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success -> {
                  resources.data?.data?.let { data ->
                      Glide.with(this).load(data.furniture.logo).apply(RequestOptions.circleCropTransform()).into(binding.furnitureIcon)
                      binding.furnitureName.text = data.furniture.name
                      binding.furnitureDetails.text = data.furniture.description
                      binding.furnitureLocation.text = data.furniture.address
                      binding.furnitureRating.rating = data.furniture.rate.toFloat()
                      binding.furnitureRateCount.text = " تقييم${data.furniture.rate_count}"

                  }
                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding.root.showSnack(it)
                    }
                }
            }
        })
        val viewPager = binding.furniturePager
        val tabLayout = binding.furnitureTab
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout,viewPager){tab,position->
            tab.text = tabArray[position]

        }.attach()

    }
}