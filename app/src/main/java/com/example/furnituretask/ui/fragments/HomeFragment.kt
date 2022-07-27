package com.example.furnituretask.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnituretask.Constants
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentHomeBinding
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.adapters.CategoryAdapter
import com.example.furnituretask.ui.adapters.DiscountsAdapter
import com.example.furnituretask.ui.adapters.OffersAdapter
import com.example.furnituretask.ui.adapters.SavesAdapter
import com.example.furnituretask.ui.adapters.NearByHomeAdapter
import com.example.furnituretask.viewModels.HomeViewModel
import com.example.furnituretask.viewModels.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment:Fragment() {
    private var binding:FragmentHomeBinding?=null
    @Inject
    lateinit var categoryAdapter:CategoryAdapter
    @Inject
   lateinit var offersAdapter: OffersAdapter
    @Inject
    lateinit var discountsAdapter: DiscountsAdapter
    @Inject
     lateinit var savesAdapter: SavesAdapter
    @Inject
    lateinit var nearByFurniture:NearByHomeAdapter
    private val viewModel:HomeViewModel by activityViewModels()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = sharedPreferences.getString(Constants.NEAR_BY_LOCATION_LAT,"")
        val lng = sharedPreferences.getString(Constants.NEAR_BY_LOCATION_LNG,"")
        if (lat!=null && lng!=null){
            viewModel.getNearByFurniture(lat, lng)
        }

        viewModel.homeLiveData.observe(viewLifecycleOwner, Observer { resources->
            when(resources){
                is Resources.Loading ->{
                    binding?.discoverShimmer?.startShimmer()
                    binding?.discountsShimmer?.startShimmer()
                    binding?.savesShimmer?.startShimmer()
                    binding?.offerShimmer?.startShimmer()
                }
                is Resources.Success ->{
                    binding?.let {
                        it.discoverShimmer.stopShimmer()
                        it.discountsShimmer.stopShimmer()
                        it.savesShimmer.stopShimmer()
                        it.offerShimmer.stopShimmer()
                        it.discoverShimmer.visibility = View.GONE
                        it.offerShimmer.visibility =View.GONE
                        it.savesShimmer.visibility =View.GONE
                        it.discountsShimmer.visibility =View.GONE
                        it.grouQr.visibility = View.VISIBLE

                        resources.data?.data?.let { data ->
                            if(data.discounts.isEmpty()){
                                it.moreDiscounts.visibility =View.GONE
                                it.discountsPager.visibility = View.GONE
                                it.homeDiscountsTxt.visibility =View.GONE

                            }
                            if (data.offers.isEmpty()){
                                it.moreOffers.visibility =View.GONE
                                it.offersPager.visibility = View.GONE
                                it.homeOfferTxt.visibility =View.GONE
                            }
                            if (data.saves.isEmpty()){
                                it.moreSaves.visibility =View.GONE
                                it.savesPager.visibility = View.GONE
                                it.homeSavesTxt.visibility =View.GONE
                            }
                            categoryAdapter.differ.submitList(data.categories)
                            offersAdapter.differ.submitList(data.offers)
                            discountsAdapter.differ.submitList(data.discounts)
                            savesAdapter.differ.submitList(data.saves)
                        }
                    }

                }
                is Resources.Failure->{
                    binding?.let {
                        it.homeRecyclerview.visibility = View.VISIBLE
                    }
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }


                }
            }
        })
viewModel.nearByLiveData.observe(viewLifecycleOwner,Observer{resources->
    when(resources){
        is Resources.Success ->{
            nearByFurniture.differ.submitList(resources.data?.data)
            binding?.furnitureGroup?.visibility= View.VISIBLE
        }
        is Resources.Failure ->{
            binding?.furnitureGroup?.visibility= View.INVISIBLE
        }
    }

})
        setupCategoryRecyclerView()
        setupNearFurnituresRecyclerView()
        setupViewPagers()
        handleNavigation()
    }

    private fun handleNavigation() {
        binding?.let {
            it.moreSaves.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                )
            }
        }
        binding?.let {
            it.moreCategory.setOnClickListener {
                findNavController().navigate(
                   HomeFragmentDirections.actionHomeFragmentToCategoryFragment()
                )
            }
        }
        binding?.let {
            it.moreDiscounts.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDiscountsFragment( )
                )
            }
        }
        binding?.let {
            it.moreAntag.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToNearByFragment()
                )
            }
        }
        binding?.let {
            it.moreOffers.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToOffersFragment()
                )
            }
        }
        binding?.let {
            it.homeAddLocation.setOnClickListener {
                postUserLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun postUserLocation() {
       if(checkPermissions()){
           if(isLocationenabled()){
               fusedLocationProviderClient = com.google.android.gms.location.FusedLocationProviderClient(requireContext())

             fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                 val location:Location? = it.result
                 if (location==null){
                     binding?.root?.showSnack(getString(R.string.error))
                 }else{
                      sharedPreferences.edit {
                          putString(Constants.NEAR_BY_LOCATION_LAT,location.latitude.toString())
                          putString(Constants.NEAR_BY_LOCATION_LNG,location.longitude.toString())
                          apply()

                      }
                     viewModel.getNearByFurniture(location.latitude.toString(),location.longitude.toString())
                 }
             }
           }else{
            binding?.root?.showSnack(getString(R.string.enable_location))
               val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
               startActivity(intent)
           }
       }else{
              requestPermissionLauncher.launch(permissions)
       }
    }

    private fun isLocationenabled(): Boolean {
      val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions():Boolean {
    if (ActivityCompat.checkSelfPermission(
            requireContext(),
           Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ){
        return true
    }
    return false
}
    var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permission->
        val granted = permission.entries.all {
            it.value
        }
        if (granted){
            postUserLocation()
        }else{
            Toast.makeText(requireContext(), getString(R.string.location_perm), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.discoverShimmer?.stopShimmer()
        binding?.discountsShimmer?.stopShimmer()
        binding?.savesShimmer?.stopShimmer()
        binding?.offerShimmer?.stopShimmer()
    }
    private fun setupCategoryRecyclerView(){
        binding?.let {
            it.homeRecyclerview.apply{
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
                adapter = categoryAdapter
            }
        }
    }
    private fun setupNearFurnituresRecyclerView(){
        binding?.let {
            it.antagRecyclerview.apply{
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
                adapter = nearByFurniture
            }
        }
    }
    private fun setupViewPagers(){
        binding?.offersPager?.let {
            it.adapter = offersAdapter
            it.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }
        binding?.savesPager?.let {
            it.adapter = savesAdapter
            it.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        }
        binding?.discountsPager?.let {
            it.adapter = discountsAdapter
            it.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        }
    }
}