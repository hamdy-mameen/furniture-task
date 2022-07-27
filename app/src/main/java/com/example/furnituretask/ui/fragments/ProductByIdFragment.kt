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
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.local.Favorite
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.CategoryAdapter
import com.example.furnituretask.ui.adapters.ProductByCateIdAdapter
import com.example.furnituretask.viewModels.MainViewModel
import com.example.furnituretask.viewModels.ProductByIdViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductByIdFragment: Fragment() {
    private var binding:ListFragmentBinding?=null
    private val viewModel:ProductByIdViewModel by viewModels()
    @Inject
    lateinit var productByCateIdAdapter: ProductByCateIdAdapter
    private val args:ProductByIdFragmentArgs by navArgs()
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
        binding?.titleToolBar?.text = "${args.name}"
        setupCategoryRecyclerView()
        viewModel.getProductByCategoryId(args.id)
        viewModel.productByIdLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                    productByCateIdAdapter.differ.submitList(resources.data?.data?.products)

                    productByCateIdAdapter.setClickListener {
                        val favorite = Favorite(it.product_id,it.images[0].path,it.name,it.description,it.price.toString())
                        viewModel.insertFavorite(favorite)
                        Toast.makeText(requireContext(), getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
            }

        })
        binding?.let{
            it.titleToolBar.setOnClickListener {
                findNavController().navigate(ProductByIdFragmentDirections.actionProductByIdFragmentToCategoryFragment())
            }
        }
    }
    private fun setupCategoryRecyclerView(){
        binding?.let {
            it.listFragmentRecyclerview.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productByCateIdAdapter
            }
        }
    }
    
}