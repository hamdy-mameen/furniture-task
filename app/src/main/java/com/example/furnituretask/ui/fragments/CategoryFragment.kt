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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnituretask.R
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.CategoryAdapter
import com.example.furnituretask.viewModels.CategoryViewModel
import com.example.furnituretask.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment: Fragment() {
    private var binding:ListFragmentBinding?=null
    private val viewModel:CategoryViewModel by activityViewModels()
    @Inject
    lateinit var categoryAdapter: CategoryAdapter
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
        setupCategoryRecyclerView()
        binding?.titleToolBar?.text =getString(R.string.categories_txt)
        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                 categoryAdapter.differ.submitList(resources.data?.data)
                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
            }

        })

           categoryAdapter.setListener { i, s ->
               findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductByIdFragment(i,s))
           }
           binding?.let{
               it.titleToolBar.setOnClickListener {
                   findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToHomeFragment())
               }
           }

        
    }
    private fun setupCategoryRecyclerView(){
        binding?.let {
            it.listFragmentRecyclerview.apply{
                layoutManager = GridLayoutManager(requireContext(),3)
                adapter = categoryAdapter
            }
        }
    }
    
}