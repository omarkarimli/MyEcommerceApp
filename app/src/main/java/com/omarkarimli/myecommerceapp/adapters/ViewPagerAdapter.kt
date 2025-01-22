package com.omarkarimli.myecommerceapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    list: ArrayList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // A custom ArrayList to hold the list of Fragments
    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // createFragment creates a Fragment instance for a specific position
    override fun createFragment(position: Int): Fragment {
        // Returns the relevant Fragment for a specific position
        return fragmentList[position]
    }
}