package com.example.cryptocurrency_tracker.recyclerview

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding
import com.example.cryptocurrency_tracker.fragments.MainScreenFragment

class RecyclerViewAdapter(
    private val arrayData: List<UserEntity>,
    mainScreenFragment: MainScreenFragment
) : RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view =
            CoinViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = arrayData.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(arrayData[position])
    }
}