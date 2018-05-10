package com.chalat.broadcastreceiverslab

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intentAction = intent.action
        var toastMessage = ""
        when (intentAction) {
            Intent.ACTION_POWER_CONNECTED -> {
                toastMessage = "Power connected!"
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                toastMessage = "Power disconnected!"
            }
            ACTION_CUSTOM_BROADCAST -> {
                toastMessage = "Custom Broadcast Received"
            }
        }
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ACTION_CUSTOM_BROADCAST = "com.chalat.broadcastreceiverslab.ACTION_CUSTOM_BROADCAST"
    }
}
