package com.example.cryptocurrency_tracker.network

import com.google.gson.annotations.SerializedName

data class MainScreenJsonResponse(
    @SerializedName(value = "current_price") val currentPrice:Double,
    val name:String,
    val symbol:String,
    val image:String,
//    @SerializedName(value = "price_change_24h") val priceChange:String,
)
