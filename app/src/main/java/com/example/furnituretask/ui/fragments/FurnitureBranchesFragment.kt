package com.example.furnituretask.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentBranchesBinding
import com.example.furnituretask.databinding.FragmentFurniturePageBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.viewModels.FurnitureViewModel
import com.example.furnituretask.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FurnitureBranchesFragment:Fragment() {
    private var _binding:FragmentBranchesBinding?=null
    private val binding get() = _binding!!
    private val viewModel: FurnitureViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBranchesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setIdViewModel.value?.let {
            viewModel.getFurnitureDetails(it)
        }

        viewModel.furnitureDetailsLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources) {
                is Resources.Loading -> Toast.makeText(requireContext(),  getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success -> {
                    resources.data?.data?.let { data ->
                    binding.branchesItemName.text =  data.furniture.address
                     binding.branchesItemLocation.text = "${data.furniture.region.name},${data.furniture.governorate.name}"
                      binding.branchesItemPhone.text = data.furniture.phone
                      binding.branchesItemHours.text = "ساعات العمل : ٍ${data.furniture.times_of_week[0].to},${data.furniture.times_of_week[0].from} "
                      if(data.branches.isNotEmpty()){
                          binding.branchesGroup.visibility = View.VISIBLE
                      }

                        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,data.branches)
                      binding.branchesSpinner.adapter = adapter

                      binding.branchesSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                          override fun onItemSelected(
                              p0: AdapterView<*>?,
                              p1: View?,
                              position: Int,
                              p3: Long
                          ) {
                              binding.branchesItemName1.text =  data.branches[position].name
                              binding.branchesItemLocation1.text = data.branches[position].address
                              binding.branchesItemPhone1.text = data.branches[position].phone
                              binding.branchesItemHours1.text = " ٍ${data.branches[position].times_of_week[0].to},${data.branches[position].times_of_week[0].from}ساعات العمل : "
                          }

                          override fun onNothingSelected(p0: AdapterView<*>?) {

                          }

                      }
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
}