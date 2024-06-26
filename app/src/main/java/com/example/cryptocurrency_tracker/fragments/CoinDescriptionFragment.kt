package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentCoinDescriptionBinding
import com.squareup.picasso.Picasso
import java.net.URI
import java.util.Locale

class CoinDescriptionFragment : Fragment() {
    private lateinit var binding: FragmentCoinDescriptionBinding

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

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
            Log.d("INSIDE COIN", "coin name = $coin")
            binding.coinName.text = coin.name
            binding.coinSymbol.text = String.format("(" + coin.symbol + ")")
            binding.coinPriceNow.text = String.format(Locale.getDefault(),"%.2f", coin.currentPrice).plus("€")
            binding.coinPriceLastDay.text = String.format(Locale.getDefault(),"%.2f", coin.priceChange).plus("€")
            // TODO: display + market cap, 24-hour trading volume and price chart

            Picasso.get().load(coin.image).into(binding.descriptionImageCoin)
            binding.favouriteBtn.setOnClickListener {
                viewModel.addToFavourites(coin)
            }

            binding.shareBtn.setOnClickListener {
                viewModel.shareCoin(coin)
                sendData(coin.name, coin.image,
                    String.format(Locale.getDefault(),"%.2f", coin.currentPrice).plus("€"))
            }

            binding.descriptionExitBtn.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                // requireActivity().onBackPressed()
            }
        }
    }

    private fun sendData(name: String, image:String, price: String ){
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND

            // Setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Look at this interesting coin")

            // Adding the crypto name and coin price in a single text extra
            putExtra(Intent.EXTRA_TEXT, "Look the price of this Crypto Coin\nCrypto name: $name\nCoin price: $price\nImage URL: $image")

            type = "text/*"
        }, null)

        startActivity(share)
    }

    private fun makeLineChart(){

    }


    companion object {
        @JvmStatic
        fun newInstance() = CoinDescriptionFragment()
    }
}
