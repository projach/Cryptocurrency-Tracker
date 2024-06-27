package com.example.cryptocurrency_tracker.fragments

import java.util.Locale
import android.view.View
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cryptocurrency_tracker.R
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentCoinDescriptionBinding
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.network.PriceChartJsonResponse
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

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
            val chart = binding.coinLinechartPrice

            val entries = makePriceChart(coin.name)
            if (entries != null) {
                val dataSet = LineDataSet(entries, "Last 7 Days Prices")

                dataSet.setDrawValues(false)
                dataSet.setDrawFilled(true)
                dataSet.lineWidth = 3f
                dataSet.fillColor = Color.GRAY
                dataSet.fillAlpha = Color.RED

                chart.xAxis.labelRotationAngle = 0f

                chart.description.isEnabled = true
                chart.description.text = "Last 7 Days Prices"
                chart.description.textColor = Color.MAGENTA
                chart.description.textSize = 10f


                chart.data = LineData(dataSet)


                chart.setTouchEnabled(true)
                chart.setPinchZoom(true)


                chart.animateX(1800, Easing.EaseInExpo)
            }

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

    private fun takeData(coin:String): PriceChartJsonResponse?{
        val URL = "https://api.coingecko.com/api/v3/coins/${coin.lowercase()}/market_chart?vs_currency=eur&days=7&interval=daily&precision=2&x-cg-demo-api-key=CG-HZhV6p1qKCxRn78hoUoky7aj"
        Log.d("URL",URL)
        val networking = Networking()
        val graph = networking.makeCall(URL)
        if (graph != null) {
            var data: PriceChartJsonResponse
            runBlocking {
                val body = graph.bodyAsText()
                data = Gson().fromJson(body, PriceChartJsonResponse::class.java)
            }
            return data
        }
        return null
    }

    private fun makePriceChart(coin: String): List<Entry>? {
        val data = takeData(coin)
        if (data != null) {
            val list = mutableListOf<Entry>()

            data.prices.map {
                list.add(Entry(it[0].toFloat(), it[1].toFloat()))
            }

            return list
        }
        return null
    }

    companion object {
        @JvmStatic
        fun newInstance() = CoinDescriptionFragment()
    }
}