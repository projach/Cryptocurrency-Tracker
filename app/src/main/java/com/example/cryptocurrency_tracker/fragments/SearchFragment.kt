package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.cryptocurrency_tracker.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}

// val client = OkHttpClient()

// val request = Request.Builder()
//    .url("https://pro-api.coingecko.com/api/v3/search?query=bitcoin")
//    .get()
//    .addHeader("accept", "application/json")
//    .build()

// val response = client.newCall(request).execute()

// TODO: search by name & symbol
// TODO: display search results in a list
// TODO: mark or unmark as favourite from search & popular fragments ! - extra -