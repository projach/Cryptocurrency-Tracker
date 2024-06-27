package com.example.cryptocurrency_tracker.network

import android.util.Log
import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class Networking {
    fun isNetworkAvailable(context: Context?): Boolean{
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            }
        }
        return false
    }

    fun makeCall(url:String) : HttpResponse?{
        val client = HttpClient(CIO)
        val response: HttpResponse

        runBlocking {
            response =
                client.get(url)
        }
        if (response.status == HttpStatusCode.OK) {
            return response
        }
        return null
    }
}