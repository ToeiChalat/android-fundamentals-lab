package com.chalat.broadcastreceiverslab

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.View
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.chalat.broadcastreceiverslab.CustomReceiver.Companion.ACTION_CUSTOM_BROADCAST
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var receiverComponentName: ComponentName? = null
    private var customReceiver = CustomReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiverComponentName = ComponentName(this, CustomReceiver::class.java)
        toggleButton.setOnCheckedChangeListener {_, isChecked ->
            if (isChecked) {
                onReceiver()
            } else {
                offReceiver()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        packageManager.setComponentEnabledSetting(
                receiverComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )
    }

    override fun onStop() {
        super.onStop()
        packageManager.setComponentEnabledSetting(
                receiverComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        )
    }

    override fun onDestroy() {
        offReceiver()
        super.onDestroy()
    }

    fun sendCustomBroadcast(view: View) {
        val customBroadcastIntent = Intent(ACTION_CUSTOM_BROADCAST)
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent)
    }

    private fun onReceiver() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(customReceiver, IntentFilter(ACTION_CUSTOM_BROADCAST))
    }

    private fun offReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(customReceiver)
    }
}
