package com.chalat.notificationlab

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri


class MainActivity : AppCompatActivity() {

    private var notificationManager: NotificationManager? = null
    private val notificationReceiver = NotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        registerReceiver(notificationReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))

        notifyButton.setOnClickListener { sendNotification() }
        updateButton.setOnClickListener { updateNotification() }
        cancelButton.setOnClickListener { cancelNotification() }

        notifyButton.isEnabled = true
        updateButton.isEnabled = false
        cancelButton.isEnabled = false
    }

    override fun onDestroy() {
        unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }

    private fun sendNotification() {
        val notificationPendingIntent = createNotificationTapIntent()
        val learnMorePendingIntent = createLearnMoreAction()
        val updatePendingIntent = createUpdateAction()
        val builder = NotificationCompat.Builder(this, "")
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_stat_name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_learn_more, "Learn More", learnMorePendingIntent)
                .addAction(R.drawable.ic_update, "Update", updatePendingIntent)
                .setContentIntent(notificationPendingIntent)
        val notification = builder.build()
        notificationManager?.notify(NOTIFICATION_ID, notification)
        notifyButton.isEnabled = false
        updateButton.isEnabled = true
        cancelButton.isEnabled = true
    }

    private fun createUpdateAction(): PendingIntent? {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        return PendingIntent.getBroadcast(
                this,
                NOTIFICATION_ID,
                updateIntent,
                PendingIntent.FLAG_ONE_SHOT
        )
    }

    private fun createLearnMoreAction(): PendingIntent? {
        val learnMoreIntent = Intent(Intent.ACTION_VIEW, Uri
                .parse(NOTIFICATION_GUIDE_URL))
        return PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                learnMoreIntent,
                PendingIntent.FLAG_ONE_SHOT
        )
    }

    fun updateNotification() {
        val androidImage = BitmapFactory
                .decodeResource(resources, R.drawable.mascot_1)
        val notificationPendingIntent = createNotificationTapIntent()
        val builder = NotificationCompat.Builder(this, "")
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_stat_name))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(notificationPendingIntent)
                .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle("Notification Updated!"))
        notificationManager?.notify(NOTIFICATION_ID, builder.build())
        notifyButton.isEnabled = false
        updateButton.isEnabled = false
        cancelButton.isEnabled = true

    }

    private fun createNotificationTapIntent(): PendingIntent? {
        val notificationIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun cancelNotification() {
        notificationManager?.cancel(NOTIFICATION_ID)
        notifyButton.isEnabled = true
        updateButton.isEnabled = false
        cancelButton.isEnabled = false
    }

    companion object {
        private const val NOTIFICATION_ID = 0
        private const val NOTIFICATION_GUIDE_URL = "https://developer.android.com/design/patterns/notifications.html"
        private const val ACTION_UPDATE_NOTIFICATION = "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION"
    }

    inner class NotificationReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updateNotification()
        }
    }


}
