package com.example.cryptocurrency_tracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptocurrency_tracker.databinding.FragmentMainScreenBinding
import com.example.cryptocurrency_tracker.network.JsonView
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.google.gson.Gson
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

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

        networking.isNetworkAvailable(context)

        val coins = networking.makeCall(coinsUrl)
        var jsonView: Array<JsonView>
        runBlocking { jsonView = Gson().fromJson(coins.bodyAsText(), Array<JsonView>::class.java) }
        Log.d("JSON ARRAY SIZE", jsonView.size.toString())
        val mContext = context
        if(mContext != null){
            binding.recyclerView.adapter = RecyclerViewAdapter(jsonView,makeIDs(jsonView.size))
        }

    }

    private fun makeIDs(size:Int): List<Int> {
        val arrayData = mutableListOf<Int>()
        (1..size).forEach {
            arrayData.add(it)
        }

        return arrayData
    }
}