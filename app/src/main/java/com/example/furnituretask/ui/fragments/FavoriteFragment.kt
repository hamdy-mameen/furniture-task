package com.example.furnituretask.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.setListener
import com.example.furnituretask.ui.adapters.*
import com.example.furnituretask.viewModels.ProductByIdViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment:Fragment() {
    private var binding:ListFragmentBinding?=null
    private val viewModel:ProductByIdViewModel by activityViewModels()
    @Inject
    lateinit var  favoriteAdapter:FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRateRecyclerView()
        viewModel.favoriteLiveData.observe(viewLifecycleOwner, Observer {
            favoriteAdapter.differ.submitList(it)
        })
    setListener {
        findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailsFragment(it))
    }
    }
    private fun setupRateRecyclerView(){
        binding?.let {
            it.listFragmentRecyclerview .apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = favoriteAdapter
            }
        }
    }
}