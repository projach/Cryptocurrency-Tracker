package com.example.cryptocurrency_tracker.network

import com.google.gson.annotations.SerializedName

data class SearchJsonResponse(
    val name:String,
    val symbol:String,
    val thumb:String
)
