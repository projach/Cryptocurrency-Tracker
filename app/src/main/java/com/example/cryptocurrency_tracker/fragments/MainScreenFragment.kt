package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import androidx.room.Room
import com.google.gson.Gson
import android.app.Activity
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import io.ktor.client.statement.bodyAsText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_tracker.R
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.database.DatabaseInstance
import com.example.cryptocurrency_tracker.network.JsonResponse
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {
    private lateinit var binding: FragmentMainScreenBinding


    private lateinit var viewModel: MyViewModel

    private val URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

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
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val networking = Networking()

        if (networking.isNetworkAvailable(context)) {
            showUIOnline()
        } else {
        //    binding.mainScreenRefresh.visibility = View.VISIBLE
//            viewModel.viewModelScope.launch {
                val databaseData = getDatabase()
                if (databaseData != null) {
                    val recyclerViewAdapter = RecyclerViewAdapter(
                        databaseData,
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
                            viewModel.removeFromFavourites(coin)
                        },
                    )
                    binding.recyclerView.adapter = recyclerViewAdapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
               }
           // }

//            binding.mainScreenRefresh.setOnClickListener {
//                if (networking.isNetworkAvailable(context)) {
//                    binding.mainScreenRefresh.visibility = View.INVISIBLE
//                    showUIOnline()
//                }
//            }
        }
    }

    private fun showUIOnline() {
      //  viewModel.viewModelScope.launch {
            val data = takeData(Networking(), URL)
            saveDatabase(data)
            val databaseData = getDatabase()
            if (databaseData != null) {
                val recyclerViewAdapter = RecyclerViewAdapter(
                    databaseData,
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
    //    }
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

//    private fun saveDatabase(data: Array<MainScreenJsonResponse>) {
//        val ctx = context
//        if (ctx != null) {
//            val database =
//                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
//                    .fallbackToDestructiveMigration()
//                    .build()
//            withContext(Dispatchers.IO) {
//                if (database.getUserDao().readAll().isEmpty()) {
//                    var id = 0
//                    data.forEach {
//                        id++
//                        database.getUserDao().save(UserEntity(id, it.name, it.symbol, it.image, it.currentPrice, false))
//                    }
//                } else {
//                    data.forEach {
//                        database.getUserDao().updateData(it.currentPrice, it.name)
//                    }
//                }
//            }
//        }
//    }

    private fun saveDatabase(data: Array<JsonResponse>) {
        val ctx = context
        if (ctx != null) {
            val database =
                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            if (database.getUserDao().readAll().isEmpty()) {
                var id = 0
                data.forEach {
                    id++
                    database.getUserDao()
                        .save(UserEntity(it.currentPrice, id, it.name, it.symbol, it.image,false,it.priceChange))
                }
            } else {
                data.forEach {
                    database.getUserDao().updateData(it.currentPrice, it.name)
                }
            }
        }
    }

//    private fun getDatabase(): List<UserEntity>? {
//        val ctx = context
//        if (ctx != null) {
//            val database =
//                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
//                    .build()
//            return withContext(Dispatchers.IO) {
//                val data = database.getUserDao().readAll()
//                if (data.isEmpty()) {
//                    null
//                } else {
//                    data
//                }
//            }
//        }
//        return null
//    }

    private fun getDatabase(): List<UserEntity>? {
        val ctx = context
        if (ctx != null) {
            val database =
                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            val data = database.getUserDao().readAll()
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
        fun newInstance() = MainScreenFragment()
    }
}