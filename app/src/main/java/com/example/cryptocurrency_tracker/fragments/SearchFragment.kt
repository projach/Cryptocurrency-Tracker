package com.example.cryptocurrency_tracker.fragments

import android.app.Activity
import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking
import io.ktor.client.statement.bodyAsText
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentSearchBinding
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_tracker.R
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.network.SearchJsonResponse
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.launch
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: MyViewModel

    private val Url = "https://api.coingecko.com/api/v3/search?query=bitcoin&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

    private val client = HttpClient(CIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     val act= activity
        viewModel = when (act) {
            is Activity -> ViewModelProvider(act).get(MyViewModel::class.java)
            else -> ViewModelProvider(this).get(MyViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // showUIOnline()

//        binding?.searchButton?.setOnClickListener {
//            val query = binding?.searchInput?.text.toString().trim()
//            if (query.isNotEmpty()) {
//                searchItem(query)
//            } else {
//                // do nothing
//            }
//        }
    }

//        viewModel.viewModelScope.launch {
//            val network = Networking()

//            val data = network.makeCall(URL)
////            val jsonData: Array<SearchJsonResponse>
////            runBlocking {
////                jsonData = Gson().fromJson(data.bodyAsText(), Array<SearchJsonResponse>::class.java)
////            }
////            Log.d("DATA FOR SEARCH", jsonData.toString())

        private fun showUIOnline() {
//            viewModel.viewModelScope.launch {
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
                        onShareClick = { coin ->
                            viewModel.shareCoin(coin)
                        },
                        onFavouriteClick = { coin ->
                            viewModel.addToFavourites(coin)
                        }
                    )
                    binding.recyclerView.adapter = recyclerViewAdapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
//            }
        }

        private fun convertData(data: Array<SearchJsonResponse>): List<UserEntity>{
            val dataToReturn: MutableList<UserEntity> = mutableListOf()
            var i = 0
            data.forEach{
                i++
                // TODO: no price and favourite in search results
                // dataToReturn.add(UserEntity(i,it.name,it.symbol,it.thumb))
            }
            return dataToReturn
        }

        private fun takeData(networking: Networking, url: String): Array<SearchJsonResponse> {
            val coins = networking.makeCall(url)
            var searchJsonResponse: Array<SearchJsonResponse>
            runBlocking {
                searchJsonResponse =
                    Gson().fromJson(coins.bodyAsText(), Array<SearchJsonResponse>::class.java)
            }
            return searchJsonResponse
        }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}