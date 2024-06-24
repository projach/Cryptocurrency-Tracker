package com.example.cryptocurrency_tracker.fragments

import android.app.Activity
import android.view.View
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentSearchBinding
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.network.SearchJsonResponse
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: MyViewModel

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

//        binding?.searchButton?.setOnClickListener {
//            val query = binding?.searchInput?.text.toString().trim()
//            if (query.isNotEmpty()) {
//                searchItem(query)
//            } else {
//                // do nothing
//            }
//        }

    }

//        private fun searchItem(query: String) {
//            // using viewModelScope to launch a coroutine
//            viewModelStore.launch(Dispatchers.IO) {
//                try {
//                    val response = client.get<String>("https://api.coingecko.com/api/v3/search?q={query}") {
//                        parameter("q", query)
//                    }
//                    Log.d("RESPONSE", response)
//
//                    val jsonResponse =
//                        Gson().fromJson(response, Array<SearchJsonResponse>::class.java)
//                    val dataList = jsonResponse.mapNotNull { it.ide.toString() }
//
//                    // update UI
//                    launch(Dispatchers.Main) {
//                        binding.recyclerView.adapter =
//                            SecondListAdapter(dataList = dataList) { value ->
//                                Log.d("TAG", "User selected the item $value")
//                                myViewModel.selectedItem = value
//                                myViewModel.streamSelectedItem.postValue(value)
//                            }
//                    }
//                } catch (e: Exception) {
//                    Log.e("ERROR", "Error performing search: ${e.message}", e)
//                }
//            }
//        }

//        runBlocking {
//            val response =
//                client.get("https://api.coingecko.com/api/v3/search") {
//                    parameter("q", query)
//                }
//            Log.d("RESPONSE", response.bodyAsText())
//
//            val jsonResponse =
//                Gson().fromJson(response.bodyAsText(), Array<SearchJsonResponse>::class.java)
//
//            val dataList = jsonResponse.mapNotNull { it.ide.toString() }
//
//            binding.networkMainRecycler.adapter =
//                SecondListAdapter(dataList = dataList, listener = { value ->
//                    Log.d("TAG", "user select the item ".plus(value))
//                    myViewModel.selectedItem = value
//                    myViewModel.streamSelectedItem.postValue(value)
//                })
//        }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}