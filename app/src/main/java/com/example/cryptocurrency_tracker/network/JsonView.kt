package com.example.cryptocurrency_tracker.network

import com.google.gson.annotations.SerializedName

data class JsonView(
    val name:String,
    val symbol:String,
    val image:String,
    @SerializedName(value = "current_price") val currentPrice:Double
)