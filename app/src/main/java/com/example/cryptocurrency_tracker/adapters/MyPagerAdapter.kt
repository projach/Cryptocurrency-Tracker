package com.example.cryptocurrency_tracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptocurrency_tracker.fragments.SearchFragment
import com.example.cryptocurrency_tracker.fragments.PopularFragment
import com.example.cryptocurrency_tracker.fragments.FavouritesFragment
import com.example.cryptocurrency_tracker.fragments.MainScreenFragment

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        MainScreenFragment(),
        PopularFragment(),
        FavouritesFragment(),
        SearchFragment()
    )

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}