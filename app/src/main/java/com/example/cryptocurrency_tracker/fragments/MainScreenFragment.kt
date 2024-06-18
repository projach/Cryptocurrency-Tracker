package com.example.cryptocurrency_tracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import io.ktor.client.statement.bodyAsText
import com.example.cryptocurrency_tracker.network.JsonView
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {
    private lateinit var binding: FragmentMainScreenBinding

    val coinsUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val networking = Networking()

        if(networking.isNetworkAvailable(context)) {
            val coins = networking.makeCall(coinsUrl)
            var jsonView: Array<JsonView>
            runBlocking {
                jsonView = Gson().fromJson(coins.bodyAsText(), Array<JsonView>::class.java)
            }
            Log.d("JSON ARRAY SIZE", jsonView.size.toString())
            val mContext = context
            if (mContext != null) {
                binding.recyclerView.adapter = RecyclerViewAdapter(jsonView, makeIDs(jsonView.size), MainScreenFragment())
            }
        }else{
            binding.mainScreenRefresh.visibility = View.VISIBLE
//          TODO("take the data from database")
            binding.mainScreenRefresh.setOnClickListener{
                if (networking.isNetworkAvailable(context)){
                    binding.mainScreenRefresh.visibility = View.INVISIBLE
//                  TODO("Make a method that does the api call and populates the data")
                }
            }
        }
    }

    private fun apiCall(){

    }

    private fun makeIDs(size:Int): List<Int> {
        val arrayData = mutableListOf<Int>()
        (1..size).forEach {
            arrayData.add(it)
        }
        return arrayData
    }
}