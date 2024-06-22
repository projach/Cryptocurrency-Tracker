package com.example.cryptocurrency_tracker.activities

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.cryptocurrency_tracker.adapters.MyPagerAdapter
import com.example.cryptocurrency_tracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val pagerAdapter = MyPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        val tabTitles = arrayOf("Home", "Popular", "Favourites", "Search")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.contentDescription = tabTitles[position]
        }.attach()
    }
}


// TODO: add ViewModel