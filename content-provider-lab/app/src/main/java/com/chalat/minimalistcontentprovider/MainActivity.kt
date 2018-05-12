package com.chalat.minimalistcontentprovider

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onCLickDisplayEntries(view: View) {
        val contentUri = Contract.CONTENT_URI
        val projection = arrayOf(Contract.CONTENT_PATH)
        val selectionClause: String?
        val selectionArgs: Array<String>?
        val sortOrder: String? = null
        when (view.id) {
            R.id.buttonDisplayAll -> {
                selectionClause = null
                selectionArgs = null
            }
            R.id.buttonDisplayFirst -> {
                selectionClause = "${Contract.WORD_ID} = ?"
                selectionArgs = arrayOf("0")
            }
            else -> {
                selectionClause = null
                selectionArgs = null
            }
        }
        val cursor = contentResolver.query(
                Uri.parse(contentUri),
                projection,
                selectionClause,
                selectionArgs,
                sortOrder
        )
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val columnsIndex = cursor.getColumnIndex(projection[0])
                do {
                    val word = cursor.getString(columnsIndex)
                    textview.append("$word\n")
                } while (cursor.moveToNext())
            } else {
                Log.d(TAG, "onClickDisplayEntries " + "No data returned.")
                textview.append("No data returned." + "\n")
            }
            cursor.close()
        } else {
            Log.d(TAG, "onClickDisplayEntries " + "Cursor is null.")
            textview.append("Cursor is null." + "\n")
        }
    }
}
