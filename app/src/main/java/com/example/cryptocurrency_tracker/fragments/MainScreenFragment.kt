package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.util.Log
import androidx.room.Room
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
import com.example.cryptocurrency_tracker.database.DatabaseInstance
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {
    private lateinit var binding: FragmentMainScreenBinding

    val viewModel: MyViewModel by activityViewModels()

    private val URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&x_cg_demo_api_key=CG-HZhV6p1qKCxRn78hoUoky7aj"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            showUIOnline(true)
        } else {
            Log.d("NO INTERNET", "WE DONT HAVE INTERNET")
            binding.mainScreenRefresh.visibility = View.VISIBLE
            showUIOnline(false)
            binding.mainScreenRefresh.setOnClickListener {
                if (networking.isNetworkAvailable(context)) {
                    binding.mainScreenRefresh.visibility = View.GONE
                    showUIOnline(true)
                }
            }
        }
    }

    private fun showUIOnline(isOnline:Boolean) {
        if (isOnline) {
            val data = takeData(Networking(), URL)
            saveDatabase(data)
        }
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
                    }
                )
                binding.recyclerView.adapter = recyclerViewAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
    }

    private fun takeData(networking: Networking, url: String): Array<JsonResponse> {
        val coins = networking.makeCall(url)
        var jsonResponse: Array<JsonResponse>
        runBlocking {
            jsonResponse =
                Gson().fromJson(coins?.bodyAsText(), Array<JsonResponse>::class.java)
        }
        return jsonResponse
    }

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
                    if (database.getUserDao().findCoinBySymbol(it.symbol) != null) {
                        database.getUserDao().updateData(it.currentPrice, it.name, it.priceChange)
                    }
                    else{
                        database.getUserDao().save(
                            UserEntity(it.priceChange,database.getUserDao().getLastId()+1,it.name,it.symbol,it.image,false,it.priceChange)
                        )
                    }
                }
            }
        }
    }

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
