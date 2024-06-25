package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.app.Activity
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentCoinDescriptionBinding

class CoinDescriptionFragment : Fragment() {
    private lateinit var binding: FragmentCoinDescriptionBinding

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

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
        binding = FragmentCoinDescriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCoin.observe(viewLifecycleOwner) { coin ->
            binding.coinName.text = coin.name
            binding.coinSymbol.text = coin.symbol
            binding.coinPrice.text = coin.currentPrice.toString()
            // TODO: display + market cap, 24-hour trading volume and price chart

            binding.favouriteBtn.setOnClickListener {
                viewModel.addToFavourites(coin)
            }

            binding.shareBtn.setOnClickListener {
                viewModel.shareCoin(coin)
            }

            binding.exitBtn.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CoinDescriptionFragment()
    }
}