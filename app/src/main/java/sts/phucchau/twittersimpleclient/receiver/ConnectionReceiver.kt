package sts.phucchau.twittersimpleclient.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectionReceiver(private val observerConnection: ObserverConnection) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (intent?.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (cm.activeNetworkInfo != null) {
                observerConnection.onChangeConnection(true)
            } else observerConnection.onChangeConnection(false)
        }
    }

    interface ObserverConnection {
        fun onChangeConnection(isConnected: Boolean)
    }
}