package com.example.cryptocurrency_tracker.recyclerview

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding

class RecyclerViewAdapter(
    private val arrayData: List<UserEntity>,
    private val viewModel: MyViewModel,
    private val onDisplayClick: (UserEntity) -> Unit
) : RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = CoinViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(view, viewModel, onDisplayClick)
    }

    override fun getItemCount(): Int = arrayData.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(arrayData[position])
    }
}