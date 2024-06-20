package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.cryptocurrency_tracker.databinding.FragmentCoinDescriptionBinding

class CoinDescriptionFragment : Fragment() {
    private lateinit var binding: FragmentCoinDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinDescriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}


// TODO: mark or unmark the cryptocurrency as a favourite
// TODO: share cryptocurrency details with other apps
// TODO: each cryptocurrency item should display its name, symbol, current price, market cap, 24-hour trading volume and price chart