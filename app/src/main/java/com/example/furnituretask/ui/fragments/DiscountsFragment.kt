package com.example.furnituretask.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnituretask.R
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.setListener
import com.example.furnituretask.ui.adapters.DiscountsAdapter
import com.example.furnituretask.ui.adapters.NearByDetailsAdapter
import com.example.furnituretask.ui.adapters.OffersAdapter
import com.example.furnituretask.ui.adapters.SavesAdapter
import com.example.furnituretask.viewModels.DiscountsViewModel
import com.example.furnituretask.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscountsFragment:Fragment(R.layout.list_fragment) {
    private var binding:ListFragmentBinding?=null
    private val viewModel:DiscountsViewModel by activityViewModels()
   @Inject
  lateinit var discountsAdapter: DiscountsAdapter


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
                binding?.listFragmentRecyclerview?.let {
                    it.adapter = discountsAdapter
                    it.layoutManager = LinearLayoutManager(requireContext())
                   setListener {
                        findNavController().navigate(DiscountsFragmentDirections.actionDiscountsFragmentToDiscountsDetailsFragment(it))
                    }
                }
                binding?.titleToolBar?.text = getString(R.string.discount_txt)

                viewModel.discountLiveData.observe(viewLifecycleOwner, Observer { resources->
                    when(resources){
                        is Resources.Success ->{
                            discountsAdapter.differ.submitList(resources.data?.data)
                        }
                        is Resources.Failure ->{
                            Log.d("TAG","error:${resources.message}")
                        }
                        is Resources.Loading -> Toast.makeText(requireContext(),  getString(R.string.loading), Toast.LENGTH_SHORT).show()
                    }
                })



        binding?.let {
            it.titleToolBar.setOnClickListener {
                findNavController().navigate(DiscountsFragmentDirections.actionDiscountsFragmentToHomeFragment())
            }
        }
    }
}