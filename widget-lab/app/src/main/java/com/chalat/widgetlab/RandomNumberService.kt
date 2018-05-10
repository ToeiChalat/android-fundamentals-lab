package com.chalat.widgetlab

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import java.util.*


class RandomNumberService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // generates random number
        Log.d("test", "Random Service onStart Command")
        val random = Random()
        val randomInt = random.nextInt(100)
        val lastUpdate = getString(R.string.random_number_place_holder, randomInt)
        // Reaches the view on widget and displays the number
//        RandomNumberWidget.updateWidget(this, lastUpdate)

        val view = RemoteViews(packageName, R.layout.random_number_widget)
        view.setTextViewText(R.id.random_number_text, lastUpdate)
        val theWidget = ComponentName(this, RandomNumberWidget::class.java)
        val manager = AppWidgetManager.getInstance(this)
        manager.updateAppWidget(theWidget, view)

        return super.onStartCommand(intent, flags, startId)
    }



}
