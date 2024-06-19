package com.example.cryptocurrency_tracker.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.example.cryptocurrency_tracker.database.DatabaseInstance
import com.example.cryptocurrency_tracker.database.UserEntity
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
        if (networking.isNetworkAvailable(context)) {
            val data = takeData(networking, coinsUrl)
            saveDatabase(data)
            binding.recyclerView.adapter =
                RecyclerViewAdapter(data, makeIDs(data.size), MainScreenFragment())
        } else {
            binding.mainScreenRefresh.visibility = View.VISIBLE
//            TODO("take the data from database")
            binding.mainScreenRefresh.setOnClickListener {
                if (networking.isNetworkAvailable(context)) {
                    binding.mainScreenRefresh.visibility = View.INVISIBLE
                    val data = takeData(networking, coinsUrl)
                    saveDatabase(data)
                    binding.recyclerView.adapter =
                        RecyclerViewAdapter(data, makeIDs(data.size), MainScreenFragment())
                }
            }
        }
    }

    private fun takeData(networking: Networking, url: String): Array<JsonView>{
        val coins = networking.makeCall(url)
        var jsonView: Array<JsonView>
        runBlocking {
            jsonView = Gson().fromJson(coins.bodyAsText(), Array<JsonView>::class.java)
        }
        return jsonView
    }

    //deletes all of the database and populates again with the new request
    private fun saveDatabase(data: Array<JsonView>){
        val ctx = context
        if(ctx != null) {
            val database =
                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
                    .allowMainThreadQueries()
                    .build()


            database.getUserDao().deleteAll()

            var id = 0

            data.forEach {
                id++
                database.getUserDao().save(UserEntity(id,it.name,it.symbol,it.image,it.currentPrice,false))
            }
        }
    }

    private fun getDatabase(): List<UserEntity>?{
        val ctx = context
        if(ctx != null) {
            val database =
                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
                    .allowMainThreadQueries()
                    .build()

            val data = database.getUserDao().readAll()

            if (data.isEmpty()){
                return null
            }else{
                return data
            }
        }
        return null
    }

    private fun makeIDs(size:Int): List<Int> {
        val arrayData = mutableListOf<Int>()
        (1..size).forEach {
            arrayData.add(it)
        }
        return arrayData
    }
}