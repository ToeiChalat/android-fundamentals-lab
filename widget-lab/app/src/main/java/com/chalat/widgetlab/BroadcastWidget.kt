package com.chalat.widgetlab

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class BroadcastWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == ACTION_BROADCAST_WIDGET) {
            counter++

            val views = RemoteViews(context.packageName, R.layout.broadcast_widget)
            views.setTextViewText(R.id.urlText, counter.toString())

            val componentName = ComponentName(context, BroadcastWidget::class.java)

            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.updateAppWidget(componentName, views)
        }
    }

    companion object {

        private const val ACTION_BROADCAST_WIDGET = "ACTION_BROADCAST_WIDGET"
        private var counter = 0

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.broadcast_widget)
            views.setTextViewText(R.id.urlText, widgetText)

            val intent = Intent(context, BroadcastWidget::class.java)
            intent.action = ACTION_BROADCAST_WIDGET
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            views.setOnClickPendingIntent(R.id.urlText, pendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

