package com.chalat.widgetlab

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews


/**
 *
 * Created by Chalat Chansima on 5/8/18.
 *
 */
class SimpleAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.simple_app_widget)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://google.co.th"))
        // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        // Here the basic operations the remote view can do.
        views.setOnClickPendingIntent(R.id.tvWidget, pendingIntent)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}