package com.example.cryptocurrency_tracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptocurrency_tracker.databinding.FragmentMainScreenBinding
import com.example.cryptocurrency_tracker.network.JsonResponse
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

class MainScreenFragment : Fragment() {
    private var binding: FragmentMainScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val client = HttpClient(CIO)
        val url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&x-cg-demo-api-key=CG-HZhV6p1qKCxRn78hoUoky7aj"
        runBlocking {
            val response =
                client.get(url)
            Log.d("RESPONSE", response.bodyAsText())

            val jsonResponse =
                Gson().fromJson(response.bodyAsText(), Array<JsonResponse>::class.java)
            Log.d("RESPONSE JSON", jsonResponse.joinToString { it.toString() })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainScreenFragment()
    }
}