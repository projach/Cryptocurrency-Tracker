package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import androidx.room.Room
import com.google.gson.Gson
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import io.ktor.client.statement.bodyAsText
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.network.Networking
import com.example.cryptocurrency_tracker.database.DatabaseInstance
import com.example.cryptocurrency_tracker.network.PopularJsonResponse
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

        if (networking.isNetworkAvailable(context)) {
            showUIOnline()
        } else {
            binding.mainScreenRefresh.visibility = View.VISIBLE
            val databaseData = getDatabase()
            if (databaseData != null) {
                binding.recyclerView.adapter =
                    RecyclerViewAdapter(
                        databaseData,
                        MainScreenFragment()
                    )
            }
            binding.mainScreenRefresh.setOnClickListener {
                if (networking.isNetworkAvailable(context)) {
                    binding.mainScreenRefresh.visibility = View.INVISIBLE
                    showUIOnline()
                }
            }
        }
    }

    private fun showUIOnline() {
        val data = takeData(Networking(), coinsUrl)
        saveDatabase(data)
        val databaseData = getDatabase()
        if (databaseData != null) {
            binding.recyclerView.adapter =
                RecyclerViewAdapter(
                    databaseData,
                    MainScreenFragment()
                )
        }
    }

    // TODO: popular or home ?
    private fun takeData(networking: Networking, url: String): Array<PopularJsonResponse> {
        val coins = networking.makeCall(url)
        var popularJsonResponse: Array<PopularJsonResponse>
        runBlocking {
            popularJsonResponse =
                Gson().fromJson(coins.bodyAsText(), Array<PopularJsonResponse>::class.java)
        }
        return popularJsonResponse
    }

    // deletes everything in the database and populates it again with the new request
    private fun saveDatabase(data: Array<PopularJsonResponse>) {
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
                        .save(UserEntity(id, it.name, it.symbol, it.image, it.currentPrice, false))
                }
            } else {
                data.forEach {
                    database.getUserDao().updateData(it.currentPrice, it.name)
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