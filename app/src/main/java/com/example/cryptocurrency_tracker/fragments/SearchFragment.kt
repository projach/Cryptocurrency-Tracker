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
import com.example.cryptocurrency_tracker.network.MainScreenJsonResponse
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.network.SearchJsonResponse
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchFragment : Fragment() {
    var URL = "https://api.coingecko.com/api/v3/search?query=bitcoin&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

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
        val network = Networking()

        val data = network.makeCall(URL)
        val jsonData: Array<SearchJsonResponse>
        runBlocking {
           jsonData = Gson().fromJson(data.bodyAsText(), Array<SearchJsonResponse>::class.java)
        }
        Log.d("DATA FOR SEARCH", jsonData.toString())


//        binding?.searchButton?.setOnClickListener {
//            val query = binding?.searchInput?.text.toString().trim()
//            if (query.isNotEmpty()) {
//                searchItem(query)
//            } else {
//                // do nothing
//            }
//        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}