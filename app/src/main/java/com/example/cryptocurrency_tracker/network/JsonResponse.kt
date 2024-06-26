package com.example.cryptocurrency_tracker.network

import com.google.gson.annotations.SerializedName

data class JsonResponse(
    @SerializedName(value = "current_price") val currentPrice:Double,
    @SerializedName(value = "price_change_24h") val priceChange:Double,
    val name:String,
    val symbol:String,
    val image:String,

)
