package com.example.cryptocurrency_tracker.fragments

import java.util.Locale
import android.view.View
import android.os.Bundle
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentCoinDescriptionBinding

class CoinDescriptionFragment : Fragment() {
    private lateinit var binding: FragmentCoinDescriptionBinding

    val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            binding.coinSymbol.text = String.format("(" + coin.symbol + ")")
            binding.coinPriceNow.text = String.format(Locale.getDefault(),"%.2f", coin.currentPrice).plus("€")
            binding.coinPriceLastDay.text = String.format(Locale.getDefault(),"%.2f", coin.priceChange).plus("€")
            Picasso.get().load(coin.image).into(binding.descriptionImageCoin)
            // TODO: + price chart

            binding.favouriteBtn.setOnClickListener {
                viewModel.addToFavourites(coin)
            }

            binding.shareBtn.setOnClickListener {
                viewModel.selectCoin(coin)
                sendData(coin.name, coin.image,
                    String.format(Locale.getDefault(),"%.2f", coin.currentPrice).plus("€"))
            }

            binding.descriptionExitBtn.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun sendData(name: String, image:String, price: String ){
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND

            putExtra(Intent.EXTRA_TITLE, "Look at this interesting coin")

            putExtra(Intent.EXTRA_TEXT, "Look the price of this Crypto Coin\nCrypto name: $name\nCoin price: $price\nImage URL: $image")

            type = "text/*"
        }, null)

        startActivity(share)
    }

    private fun makePriceChart(){

    }

    companion object {
        @JvmStatic
        fun newInstance() = CoinDescriptionFragment()
    }
}