package com.example.cryptocurrency_tracker.recyclerview

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.example.cryptocurrency_tracker.R
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding

class RecyclerViewHolder(
    val binding: CoinViewBinding,
    private val onDisplayClick: (UserEntity) -> Unit,
    private val onShareClick: (UserEntity) -> Unit,
    private val onFavouriteClick: (UserEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: UserEntity){
        val imageV: ImageView = binding.viewImageCoin
        Picasso.get().load(data.image).into(imageV)
        binding.viewTextPrice.text = data.currentPrice.toString().plus("€")
        binding.viewTextId.text = data.id.toString()
        binding.viewTextSymbol.text = data.symbol

//        binding.constraintLayoutCoin.setOnClickListener{
////      TODO: Add button functionality to send over to details ?
//        }

        binding.displayBtn.setOnClickListener {
            onDisplayClick(data)
        }

        binding.shareBtn.setOnClickListener {
            onShareClick(data)
        }

        binding.favouriteBtn.setOnClickListener {
            onFavouriteClick(data)
        }

        if (data.favourite) {
            binding.favouriteBtn.setImageResource(R.drawable.favourite_filled)
        } else {
            binding.favouriteBtn.setImageResource(R.drawable.favourite_unfilled)
        }
    }
}