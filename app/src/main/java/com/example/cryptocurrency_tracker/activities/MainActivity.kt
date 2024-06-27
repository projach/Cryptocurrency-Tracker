package com.example.cryptocurrency_tracker.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.R
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.adapters.MyPagerAdapter
import com.example.cryptocurrency_tracker.databinding.ActivityMainBinding
import com.example.cryptocurrency_tracker.fragments.CoinDescriptionFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        val pagerAdapter = MyPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        val tabTitles = arrayOf("Home", "Popular", "Favourites", "Search")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.contentDescription = tabTitles[position]
        }.attach()

        viewModel.selectedCoin.observe(this) { coin ->
            if (coin != null) {
                binding.viewPager.visibility = View.GONE
                binding.descriptionFragment.visibility = View.VISIBLE
            } else {
                binding.viewPager.visibility = View.VISIBLE
                binding.descriptionFragment.visibility = View.GONE
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.description_fragment)
                if (currentFragment is CoinDescriptionFragment) {
                    binding.viewPager.visibility = View.VISIBLE
                    supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}


// TODO: edit ViewPager ?
// TODO: viewModelScope ?
// TODO: update UI instantly / icons + notifications