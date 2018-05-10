package com.chalat.standup

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.os.SystemClock
import com.chalat.standup.AlarmReceiver.Companion.NOTIFICATION_ID


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val notifyIntent = Intent(ACTION_NOTIFY)
        val notifyPendingIntent = PendingIntent.getBroadcast(
                this,
                NOTIFICATION_ID,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val nextAlarm =  alarmManager.nextAlarmClock
        if (nextAlarm != null) {
            val text = "${getText(R.string.stand_up)} $nextAlarm"
            textView.text = text
        }

        val alarmUp = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null

        alarmToggle.isChecked = alarmUp

        alarmToggle.setOnCheckedChangeListener { _, isChecked ->
            toastMessage(isChecked)
            if (isChecked) {
                setAlarmOn(alarmManager, notifyPendingIntent)
            } else {
                removeAlarm(alarmManager, notifyPendingIntent)
            }
        }
    }

    private fun removeAlarm(alarmManager: AlarmManager, notifyPendingIntent: PendingIntent?) {
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun setAlarmOn(alarmManager: AlarmManager, notifyPendingIntent: PendingIntent?) {
        val triggerTime = SystemClock.elapsedRealtime()

        val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES

        //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime, repeatInterval, notifyPendingIntent)
    }

    private fun toastMessage(isChecked: Boolean) {
        val toastMessage: String = if (isChecked) {
            //Set the toast message for the "on" case
            getString(R.string.alarm_on_toast)
        } else {
            //Set the toast message for the "off" case
            getString(R.string.alarm_off_toast)
        }

        //Show a toast to say the alarm is turned on or off
        Toast.makeText(this@MainActivity, toastMessage, Toast.LENGTH_SHORT)
                .show()
    }

    companion object {
        const val ACTION_NOTIFY = "com.chalat.standup.ACTION_NOTIFY"
    }
}
