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
    private val onDisplayClick: (UserEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: UserEntity) {
        val imageV: ImageView = binding.viewImageCoin
        Picasso.get().load(data.image).into(imageV)
        binding.viewTextSymbol.text = data.symbol
        binding.viewTextPrice.text = String.format(Locale.getDefault(),"%.2f", data.currentPrice).plus("€")

        val addedToFavourites = viewModel.addedToFavourites(data.symbol)
        updateFavouriteIcon(addedToFavourites)

        binding.displayBtn.setOnClickListener {
            onDisplayClick(data)
        }

        binding.favouriteBtn.setOnClickListener {
            val newFavouriteStatus = !viewModel.addedToFavourites(data.symbol)
            updateFavouriteIcon(newFavouriteStatus)
            viewModel.handleFavourite(data)
        }
    }

    private fun updateFavouriteIcon(addedToFavourites: Boolean) {
        val iconRes = if (addedToFavourites) R.drawable.favourite_filled else R.drawable.favourite_unfilled
        binding.favouriteBtn.setImageResource(iconRes)
    }
}