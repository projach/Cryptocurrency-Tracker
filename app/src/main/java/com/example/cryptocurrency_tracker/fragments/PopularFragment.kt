package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import com.google.gson.Gson
import android.app.Activity
import android.view.ViewGroup
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.viewModelScope
import io.ktor.client.statement.bodyAsText
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.R
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.network.MainScreenJsonResponse
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentPopularBinding

class PopularFragment : Fragment() {
    private lateinit var binding: FragmentPopularBinding

    private lateinit var viewModel: MyViewModel

    // returns the most popular coins by market cap
    private val URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

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
        showUIOnline()
    }

    private fun showUIOnline() {
        viewModel.viewModelScope.launch {
            val data = takeData(Networking(), URL)
            if (data != null) {
                val recyclerViewAdapter = RecyclerViewAdapter(
                    convertData(data),
                    viewModel,
                    onDisplayClick = { coin ->
                        viewModel.selectCoin(coin)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.description_fragment, CoinDescriptionFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    },
                    onShareClick = { coin ->
                        viewModel.shareCoin(coin)
                    },
                    onFavouriteClick = { coin ->
                        viewModel.addToFavourites(coin)
                    }
                )
                binding.recyclerView.adapter = recyclerViewAdapter
            }
        }
    }

    private fun convertData(data: Array<MainScreenJsonResponse>): List<UserEntity>{
        val dataToReturn: MutableList<UserEntity> = mutableListOf()
        var i = 0
        data.forEach{
            i++
            dataToReturn.add(UserEntity(i,it.name,it.symbol,it.image,it.currentPrice,false))
        }
        return dataToReturn
    }

    private fun takeData(networking: Networking, url: String): Array<MainScreenJsonResponse> {
        val coins = networking.makeCall(url)
        var mainScreenJsonResponse: Array<MainScreenJsonResponse>
        runBlocking {
            mainScreenJsonResponse =
                Gson().fromJson(coins.bodyAsText(), Array<MainScreenJsonResponse>::class.java)
        }
        return mainScreenJsonResponse
    }

    companion object {
        @JvmStatic
        fun newInstance() = PopularFragment()
    }
}


// TODO: each cryptocurrency item should display its name, symbol, current price and price change