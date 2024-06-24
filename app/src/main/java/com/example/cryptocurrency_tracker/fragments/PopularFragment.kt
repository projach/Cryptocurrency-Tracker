package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.app.Activity
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentPopularBinding

class PopularFragment : Fragment() {
    private lateinit var binding: FragmentPopularBinding

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val act = activity
        viewModel = when (act) {
            is Activity -> ViewModelProvider(act).get(MyViewModel::class.java)
            else -> ViewModelProvider(this).get(MyViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance() = PopularFragment()
    }
}


// TODO: each cryptocurrency item should display its name, symbol, current price and price change