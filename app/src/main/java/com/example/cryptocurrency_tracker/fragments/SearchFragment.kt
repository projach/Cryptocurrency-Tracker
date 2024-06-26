package com.example.cryptocurrency_tracker.fragments

import android.app.Activity
import android.view.View
import android.os.Bundle
import android.text.Editable
import android.view.ViewGroup
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.FragmentSearchBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.cryptocurrency_tracker.R
import com.example.cryptocurrency_tracker.database.DatabaseInstance
import com.example.cryptocurrency_tracker.database.UserEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: MyViewModel

    private val Url =
        "https://api.coingecko.com/api/v3/search?query=bitcoin&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

    private val client = HttpClient(CIO)

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
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nameSymb = ""
        showUIOnline(nameSymb)
        binding.searchInput.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameSymb = s.toString()
                showUIOnline(nameSymb)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
//        binding?.searchButton?.setOnClickListener {
//            val query = binding?.searchInput?.text.toString().trim()
//            if (query.isNotEmpty()) {
//                searchItem(query)
//            } else {
//                // do nothing
//            }
//        }

    }

    private fun showUIOnline(nameSymb:String) {
//            viewModel.viewModelScope.launch {
        val data = getDatabase(nameSymb)
        Log.d("INSIDE SHOW UI", data.toString())
        if (data != null) {
            val recyclerViewAdapter = RecyclerViewAdapter(
                data,
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
    }
//            }


    private fun getDatabase(nameSymb:String): List<UserEntity>? {
        val ctx = context
        if (ctx != null) {
            val database =
                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            val data = database.getUserDao().search(nameSymb)
            Log.d("INSIDE DATABASE", data.toString())
            if (data.isEmpty()) {
                return null
            } else {
                return data
            }
        }
        return null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}