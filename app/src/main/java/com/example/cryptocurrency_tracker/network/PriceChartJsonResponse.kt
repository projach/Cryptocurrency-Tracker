package com.example.cryptocurrency_tracker.network

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class PriceChartJsonResponse(
    @SerializedName(value = "prices") val prices: List<List<Double>>
):Serializable
