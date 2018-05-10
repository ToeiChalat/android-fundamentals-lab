package com.chalat.widgetlab

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.app.PendingIntent
import android.net.Uri
import android.widget.RemoteViews



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        setContentView(R.layout.activity_main)

        val widgetManager = AppWidgetManager.getInstance(this)
        val views = RemoteViews(this.packageName, R.layout.configuration_widget)
        // Find the widget id from the intent.
        var appWidgetId : Int? = null
        intent.extras?.let {
            appWidgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        btAdd.setOnClickListener {
            // Gets user input
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(etUrl.text.toString()))
            val pending = PendingIntent.getActivity(this@MainActivity, 0, intent, 0)
            views.setTextViewText(R.id.urlText, etUrl.text.toString())
            views.setOnClickPendingIntent(R.id.urlText, pending)
            appWidgetId?.let {
                widgetManager.updateAppWidget(it, views)
                val resultValue = Intent()
                // Set the results as expected from a 'configure activity'.
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, it)
                setResult(Activity.RESULT_OK, resultValue)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
