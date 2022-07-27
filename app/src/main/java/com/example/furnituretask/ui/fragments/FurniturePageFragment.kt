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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentFurniturePageBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.DiscountsAdapter
import com.example.furnituretask.ui.adapters.MenuAdapter
import com.example.furnituretask.ui.adapters.OffersAdapter
import com.example.furnituretask.ui.adapters.SavesAdapter
import com.example.furnituretask.viewModels.FurnitureViewModel
import com.example.furnituretask.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class FurniturePageFragment:Fragment() {
    private var _binding:FragmentFurniturePageBinding?=null
    private val binding get() = _binding!!
    private val viewModel:FurnitureViewModel by activityViewModels()
    @Inject
     lateinit var menuAdapter: MenuAdapter
    @Inject
     lateinit var offersAdapter: OffersAdapter
    @Inject
     lateinit var discountsAdapter: DiscountsAdapter
    @Inject
    lateinit var savesAdapter: SavesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFurniturePageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuReceyclerView()
        setupViewPagers()
        viewModel.setIdViewModel.value?.let {
            viewModel.getFurnitureDetails(it)
        }
        viewModel.furnitureDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources) {
                is Resources.Loading -> Toast.makeText(requireContext(),  getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success -> {
                    resources.data?.data?.let { data ->
                     if(data.menu.isNotEmpty()){
                         menuAdapter.differ.submitList(data.menu)
                     }else{
                         binding.groupMenu.visibility = View.GONE
                     }
                        if(data.discounts.isNotEmpty()){
                        discountsAdapter.differ.submitList(data.discounts)
                        }else{
                           binding.pageDiscountsTxt.visibility = View.INVISIBLE
                        }
                        offersAdapter.differ.submitList(data.offers)
                        savesAdapter.differ.submitList(data.saves)

                    }
                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding.root.showSnack(it)
                    }
                }
            }
        })
    }
    private fun setupMenuReceyclerView(){
      binding.pageMenuRecyclerview.apply {
          layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
          adapter = menuAdapter
      }
    }
    private fun setupViewPagers(){
        binding.pageOffersPager.apply {
            adapter = offersAdapter

            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }
        binding.pageSavesPager .apply {
            adapter = savesAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        }
        binding.pageDiscountsPager.apply {
            adapter = discountsAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        }
    }
}