package com.example.cryptocurrency_tracker.recyclerview

import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding
import com.example.cryptocurrency_tracker.fragments.MainScreenFragment
import com.example.cryptocurrency_tracker.network.JsonView
import com.squareup.picasso.Picasso

class RecyclerViewHolder(val binding: CoinViewBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: JsonView, id:Int){
        val imageV: ImageView = binding.viewImageCoin
        Picasso.get().load(data.image).into(imageV)
        binding.viewTextPrice.text = data.currentPrice.toString().plus("€")
        binding.viewTextId.text = id.toString()
        binding.viewTextSymbol.text = data.symbol
        binding.constraintLayoutCoin.setOnClickListener{
//            TODO("Add button functionality to send over to details")
        }
    }
}