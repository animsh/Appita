package com.animsh.appita.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by animsh on 3/17/2021.
 */
class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkConnected = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        isNetworkConnected.value = isConnected
        return isNetworkConnected
    }

    override fun onAvailable(network: Network) {
        isNetworkConnected.value = true
    }

    override fun onLost(network: Network) {
        isNetworkConnected.value = false
    }
}