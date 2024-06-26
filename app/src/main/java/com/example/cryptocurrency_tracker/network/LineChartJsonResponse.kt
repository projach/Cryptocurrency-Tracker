package com.example.cryptocurrency_tracker.network

import com.google.gson.annotations.SerializedName

data class LineChartJsonResponse(
    @SerializedName(value = "current_price") val currentPrice: Double,
    val name: String,
    val symbol: String,
    val image: String
)


// maybe not needed !