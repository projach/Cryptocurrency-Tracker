package com.example.cryptocurrency_tracker.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PriceChartJsonResponse(
    @SerializedName(value = "prices") val prices: List<List<Double>>
):Serializable
