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
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.DiscountsAdapter
import com.example.furnituretask.ui.adapters.NearByDetailsAdapter
import com.example.furnituretask.ui.adapters.OffersAdapter
import com.example.furnituretask.ui.adapters.SavesAdapter
import com.example.furnituretask.viewModels.DiscountsViewModel
import com.example.furnituretask.viewModels.MainViewModel
import com.example.furnituretask.viewModels.OffersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OffersFragment:Fragment(R.layout.list_fragment) {
    private var binding:ListFragmentBinding?=null
    private val viewModel:OffersViewModel by activityViewModels()
   @Inject
   lateinit var offersAdapter: OffersAdapter


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
        binding?.titleToolBar?.text = getString(R.string.offer_txt)
        binding?.listFragmentRecyclerview?.let {
            it.adapter = offersAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            setListener {
                findNavController().navigate(OffersFragmentDirections.actionOffersFragmentToOfferDetailsFragment(it))
            }
        }
        viewModel.offerLiveData.observe(viewLifecycleOwner, Observer { resources->
            when(resources){
                is Resources.Success ->{
                    offersAdapter.differ.submitList(resources.data?.data)
                }
                is Resources.Failure ->{
                    Log.d("TAG","error:${resources.message}")
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
                is Resources.Loading -> Toast.makeText(requireContext(),  getString(R.string.loading), Toast.LENGTH_SHORT).show()
            }
        })


binding?.let {
    it.titleToolBar.setOnClickListener {
        findNavController().navigate(OffersFragmentDirections.actionOffersFragmentToHomeFragment())
    }
}
}
}