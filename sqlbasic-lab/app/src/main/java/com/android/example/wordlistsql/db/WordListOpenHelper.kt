package com.android.example.wordlistsql.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.database.DatabaseUtils


/**
 *
 * Created by Chalat Chansima on 5/11/18.
 *
 */
class WordListOpenHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var writableDB: SQLiteDatabase? = null
    private var readableDB: SQLiteDatabase? = null

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(WORD_LIST_TABLE_CREATE)
        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.w(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data")
        db?.execSQL(WORD_LIST_TABLE_DROP)
        onCreate(db)
    }

    fun query(position: Int): WordItem? {
        val query = "SELECT  * FROM $WORD_LIST_TABLE ORDER BY $KEY_WORD ASC LIMIT $position,1"
        var cursor: Cursor? = null
        var entry: WordItem? = null
        try {
            readableDB = readableDB ?: readableDatabase
            cursor = readableDB?.rawQuery(query, null)
            cursor?.moveToFirst()
            cursor?.let {
                val id = it.getInt(it.getColumnIndex(KEY_ID))
                val word = it.getString(it.getColumnIndex(KEY_WORD))
                entry = WordItem(id, word)
            }
        } catch (error: Exception) {
            Log.e(TAG, "query error", error)
        } finally {
            cursor?.close()
            return entry
        }
    }

    fun insert(word: String): Long {
        var newId: Long? = null
        val values = ContentValues()
        values.put(KEY_WORD, word)
        try {
            writableDB = writableDB ?: writableDatabase
            newId = writableDB?.insert(WORD_LIST_TABLE, null, values)
        } catch (exception: Exception) {
            Log.e(TAG, "query error", exception)
        }
        return newId ?: 0L
    }

    fun delete(wordId: Int): Int {
        var deleted = 0
        try {
            writableDB = writableDatabase ?: writableDatabase
            writableDB?.let {
                deleted = it.delete(
                        WORD_LIST_TABLE,
                        "$KEY_ID = ? ",
                        arrayOf(wordId.toString())
                )
            }
        } catch (exception: Exception) {
            Log.e(TAG, "delete error", exception)
        }
        return deleted
    }

    fun update(wordId: Int, word: String): Int {
        var numberOfRowsUpdated = -1
        try {
            writableDB = writableDB ?: writableDatabase
            val values = ContentValues()
            values.put(KEY_WORD, word)
            writableDB?.let {
                numberOfRowsUpdated = it.update(WORD_LIST_TABLE,
                        values,
                        "$KEY_ID = ?",
                        arrayOf(wordId.toString())
                )
            }
        } catch (exception: Exception) {
            Log.e(TAG, "update error", exception)
        }
        return numberOfRowsUpdated
    }

    fun count(): Long {
        readableDB = readableDB ?: readableDatabase
        return DatabaseUtils.queryNumEntries(readableDB, WORD_LIST_TABLE)
    }

    private fun seedData(db: SQLiteDatabase?) {
        val words = arrayOf(
                "Android",
                "Adapter",
                "ListView",
                "AsyncTask",
                "Android Studio",
                "SQLiteDatabase",
                "SQLOpenHelper",
                "Data model",
                "ViewHolder",
                "Android Performance",
                "OnClickListener"
        )

        // Create a container for the data.
        val values = ContentValues()

        for (word in words) {
            // Put column/value pairs into the container.
            // put() overrides existing values.
            values.put(KEY_WORD, word)
            db?.insert(WORD_LIST_TABLE, null, values)
        }
    }

    companion object {

        private val TAG = WordListOpenHelper::class.java.name

        private const val DATABASE_NAME = "com.chalat.wordlist.database"
        private const val WORD_LIST_TABLE = "word_entries"
        private const val DATABASE_VERSION = 1

        private const val KEY_ID = "_id"
        private const val KEY_WORD = "word"

        private val COLUMNS = arrayOf(KEY_ID, KEY_WORD)

        private const val WORD_LIST_TABLE_CREATE = "CREATE TABLE $WORD_LIST_TABLE ($KEY_ID INTEGER PRIMARY KEY, $KEY_WORD TEXT);"
        private const val WORD_LIST_TABLE_DROP = "DROP TABLE IF EXISTS $WORD_LIST_TABLE"
    }
}
