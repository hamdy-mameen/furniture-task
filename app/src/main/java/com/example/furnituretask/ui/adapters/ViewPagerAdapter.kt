package com.example.furnituretask.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.furnituretask.Constants
import com.example.furnituretask.ui.fragments.FurnitureBranchesFragment
import com.example.furnituretask.ui.fragments.FurniturePageFragment
import com.example.furnituretask.ui.fragments.FurnitureRateFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,lifeCycle:Lifecycle
):FragmentStateAdapter(fragmentManager,lifeCycle) {
    override fun getItemCount(): Int {
        return Constants.NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0-> return FurnitureBranchesFragment()
            1-> return FurnitureRateFragment()

        }
        return FurniturePageFragment()
    }


}