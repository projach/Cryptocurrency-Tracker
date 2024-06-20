package com.example.cryptocurrency_tracker.recyclerview

import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.network.JsonView
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding

class RecyclerViewAdapter(private val arrayData: Array<JsonView>,private val arrayID: List<Int>) : RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view =
            CoinViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = arrayData.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(arrayData[position], arrayID[position])
    }
}