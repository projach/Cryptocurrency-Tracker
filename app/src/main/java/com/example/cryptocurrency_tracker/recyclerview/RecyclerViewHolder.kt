package com.example.cryptocurrency_tracker.recyclerview

import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding
import com.example.cryptocurrency_tracker.network.JsonView
import com.squareup.picasso.Picasso

class RecyclerViewHolder(val binding: CoinViewBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: JsonView){
        val imageV: ImageView = binding.viewImageCoin
        Picasso.get().load(data.image).into(imageV)
        binding.viewTextPrice.text = data.currentPrice.toString()
        Log.d("VIEW PRICE", data.currentPrice.toString())
    }
}