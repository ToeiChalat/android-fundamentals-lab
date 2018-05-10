package com.chalat.asynctasklab

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startTask()
        }
    }

    private fun startTask() {
        textView.text = "Start Task..."
        MyAsyncTask(textView).execute()
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

    class MyAsyncTask(private val textView: TextView) : AsyncTask<Unit, Unit, String>() {

        override fun doInBackground(vararg params: Unit?): String {
            val r = Random()
            val n = r.nextInt(11)

            // Make the task take long enough that we have
            // time to rotate the phone while it is running
            val s = n * 200

            // Sleep for the random amount of time
            try {
                Thread.sleep(s.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }


            // Return a String result
            return "Awake at last after sleeping for $s milliseconds!"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            textView.text = result
        }
    }
}
