package com.example.furnituretask.ui.fragments

import android.content.SharedPreferences
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnituretask.Constants
import com.example.furnituretask.R
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.setListener
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.NearByDetailsAdapter
import com.example.furnituretask.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NearByFragment: Fragment() {
    private var binding:ListFragmentBinding?=null
    private val viewModel:HomeViewModel by activityViewModels()
    @Inject
     lateinit var nearByDetailsAdapter: NearByDetailsAdapter
    @Inject
    lateinit var sharedPreferences: SharedPreferences
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
        setupNearByRecyclerView()
        val lat = sharedPreferences.getString(Constants.NEAR_BY_LOCATION_LAT,"")
        val lng = sharedPreferences.getString(Constants.NEAR_BY_LOCATION_LNG,"")
        if (lat!=null && lng!=null){
            viewModel.getNearByFurniture(lat, lng)
        }
        viewModel.nearByLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(),  getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                 nearByDetailsAdapter.differ.submitList(resources.data?.data)
                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
            }

        })

        setListener {
            findNavController().navigate(NearByFragmentDirections.actionNearByFragmentToFurnitureDetailsFragment(it))
        }
        
    }
    private fun setupNearByRecyclerView(){
        binding?.let {
            it.listFragmentRecyclerview.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = nearByDetailsAdapter
            }
        }
    }
    
}