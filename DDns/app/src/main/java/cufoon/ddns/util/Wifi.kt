package cufoon.ddns.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.Nullable

object WifiTool {
    @Nullable
    fun getSystemIP(context: Context): String {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(activeNetwork)
        val linkAddresses = linkProperties?.linkAddresses
        return linkAddresses?.get(linkAddresses.size - 1).toString().split('/').first()
    }
}
