package com.example.cryptocurrency_tracker.recyclerview

import java.util.Locale
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.example.cryptocurrency_tracker.R
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.databinding.CoinViewBinding

class RecyclerViewHolder(
    val binding: CoinViewBinding,
    private val viewModel: MyViewModel,
    private val onDisplayClick: (UserEntity) -> Unit,
    private val onShareClick: (UserEntity) -> Unit,
    private val onFavouriteClick: (UserEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: UserEntity) {
        val imageV: ImageView = binding.viewImageCoin
        Picasso.get().load(data.image).into(imageV)
        binding.viewTextPrice.text = String.format(Locale.getDefault(),"%.2f", data.currentPrice).plus("€")
        binding.viewTextSymbol.text = data.symbol

        updateFavouriteIcon(data.id)

        binding.constraintLayoutCoin.setOnClickListener {
            onDisplayClick(data)
        }

        binding.shareBtn.setOnClickListener {
            onShareClick(data)
        }

        binding.favouriteBtn.setOnClickListener {
            val updatedFavouriteState = !data.favourite
            onFavouriteClick(data.copy(favourite = updatedFavouriteState))

            if (updatedFavouriteState) {
                viewModel.addToFavourites(data)
            } else {
                viewModel.removeFromFavourites(data)
            }
            updateFavouriteIcon(data.id)
        }
    }

    private fun updateFavouriteIcon(coinId: Int) {
        val addedToFavourites = viewModel.addedToFavourites(coinId)
        val iconRes = if (addedToFavourites) R.drawable.favourite_filled else R.drawable.favourite_unfilled
        binding.favouriteBtn.setImageResource(iconRes)
    }
}