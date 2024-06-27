package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import com.google.gson.Gson
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import io.ktor.client.statement.bodyAsText
import com.example.cryptocurrency_tracker.R
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.network.JsonResponse
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentPopularBinding

class PopularFragment : Fragment() {
    private lateinit var binding: FragmentPopularBinding

    val viewModel: MyViewModel by activityViewModels()

    private val Url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val data = takeData(Networking(), Url)
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
                    onFavouriteClick = { coin ->
                        viewModel.addToFavourites(coin)
                    }
                )
                binding.recyclerView.adapter = recyclerViewAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
    }

    private fun convertData(data: Array<JsonResponse>): List<UserEntity>{
        val dataToReturn: MutableList<UserEntity> = mutableListOf()
        var i = 0
        data.forEach{
            i++
            dataToReturn.add(UserEntity(it.currentPrice, id, it.name, it.symbol, it.image, false, it.priceChange))
        }
        return dataToReturn
    }

    private fun takeData(networking: Networking, url: String): Array<JsonResponse> {
        val coins = networking.makeCall(url)
        var jsonResponse: Array<JsonResponse>
        runBlocking {
            jsonResponse =
                Gson().fromJson(coins.bodyAsText(), Array<JsonResponse>::class.java)
        }
        return jsonResponse
    }

    companion object {
        @JvmStatic
        fun newInstance() = PopularFragment()
    }
}