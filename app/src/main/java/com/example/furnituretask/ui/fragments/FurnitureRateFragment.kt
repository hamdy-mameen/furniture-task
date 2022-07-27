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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentFurniturePageBinding
import com.example.furnituretask.databinding.FragmentFurnitureRateBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.MenuAdapter
import com.example.furnituretask.ui.adapters.RatesAdapter
import com.example.furnituretask.viewModels.FurnitureViewModel
import com.example.furnituretask.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class FurnitureRateFragment:Fragment() {
    private var _binding:FragmentFurnitureRateBinding?=null
    private val binding get() = _binding!!
    private val viewModel: FurnitureViewModel by activityViewModels()
    @Inject
     lateinit var ratesAdapter: RatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFurnitureRateBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuReceyclerView()
        viewModel.setIdViewModel.value?.let {
            viewModel.getFurnitureDetails(it)
        }
        viewModel.furnitureDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources) {
                is Resources.Loading -> Toast.makeText(requireContext(),  getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success -> {
                    resources.data?.data?.let { data ->
                     binding.furnitureRates.text =getString(R.string.rate) + "${data.furniture.rate_count}"
                     ratesAdapter.differ.submitList(data.rates)
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
        binding.furnitureRatestxtRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            adapter = ratesAdapter
        }
    }
}