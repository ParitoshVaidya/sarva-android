package com.supersnippets.sarva.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor(
    context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            println("-- throwing e")
            throw NoInternetException("Make sure you have an active data connection")
        }

        println("-- sending request")
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    // existing code being used in project
    private fun isInternetAvailableBak(): Boolean {
        var wifiDataAvailable = false
        var mobileDataAvailable = false
        val conManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conManager.allNetworkInfo
        for (netInfo in networkInfo) {
            if (netInfo.typeName
                    .equals("WIFI", ignoreCase = true)
            ) if (netInfo.isConnected) wifiDataAvailable = true
            if (netInfo.typeName
                    .equals("MOBILE", ignoreCase = true)
            ) if (netInfo.isConnected) mobileDataAvailable = true
        }
        return wifiDataAvailable || mobileDataAvailable
    }

}