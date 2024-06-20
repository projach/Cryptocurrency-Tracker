package com.example.cryptocurrency_tracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.example.cryptocurrency_tracker.fragments.MainScreenFragment
import com.example.cryptocurrency_tracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val fragmentContainer: FragmentContainerView? = binding?.mainFragmentContainer
        if (fragmentContainer != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(fragmentContainer.id, MainScreenFragment())
            fragmentTransaction.commit()
        }
    }
}

// TODO: add ViewPager