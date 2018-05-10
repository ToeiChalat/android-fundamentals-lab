package com.chalat.widgetlab

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.util.Log
import android.widget.RemoteViews
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class RandomNumberWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == ACTION_BROADCAST_WIDGET) {
            Log.d("test", "Random Service onStart Command")
            val random = Random()
            val randomInt = random.nextInt(100)
            val lastUpdate = context.getString(R.string.random_number_place_holder, randomInt)
            // Reaches the view on widget and displays the number
//        RandomNumberWidget.updateWidget(this, lastUpdate)

            val view = RemoteViews(context.packageName, R.layout.random_number_widget)
            view.setTextViewText(R.id.random_number_text, lastUpdate)
            val theWidget = ComponentName(context, RandomNumberWidget::class.java)
            val manager = AppWidgetManager.getInstance(context)
            manager.updateAppWidget(theWidget, view)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        setUpWidget(context)
    }

    companion object {
        private const val ACTION_BROADCAST_WIDGET = "ACTION_BROADCAST_WIDGET"

        fun setUpWidget(context: Context) {
            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, RandomNumberWidget::class.java)
            intent.action = ACTION_BROADCAST_WIDGET
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            manager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    60000,
                    pendingIntent
            )
            Log.d("test", "Random widget setUp")
        }
    }

}

