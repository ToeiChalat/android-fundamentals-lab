package com.android.example.wordlistsqlwithcontentprovider.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.database.DatabaseUtils
import android.database.MatrixCursor
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.ALL_ITEMS
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.DATABASE_NAME
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.WordList.KEY_ID
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.WordList.KEY_WORD
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.WordList.WORD_LIST_TABLE


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

    fun query(position: Int): Cursor? {
        val query = if (position != ALL_ITEMS) {
            val databasePosition = position + 1 // Because database starts counting at 1.
            "SELECT $KEY_ID,$KEY_WORD FROM $WORD_LIST_TABLE WHERE $KEY_ID=$databasePosition;"
        } else {
            "SELECT  * FROM $WORD_LIST_TABLE ORDER BY $KEY_WORD ASC "
        }

        var cursor: Cursor? = null
        try {
            readableDB = readableDB ?: readableDatabase
            cursor =readableDB?.rawQuery(query, null)
        } catch (error: Exception) {
            Log.e(TAG, "query error", error)
        } finally {
            return cursor
        }
    }

    fun insert(values: ContentValues): Long {
        var newId: Long? = null
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

    fun search(word: String): Cursor? {
        val columns = arrayOf(KEY_WORD)
        val searchString = "%$word%"
        val where = "$KEY_WORD LIKE ?"
        val whereArgs = arrayOf(searchString)
        var cursor : Cursor? = null
        try {
            readableDB = readableDB ?: readableDatabase
            cursor = readableDB?.query(
                    WordListContract.WordList.WORD_LIST_TABLE,
                    columns,
                    where,
                    whereArgs,
                    null,
                    null,
                    null
            )
        } catch (exception: Exception) {
            Log.e(TAG, "query error", exception)
        }
        return cursor
    }

    fun count(): Cursor? {
        val cursor = MatrixCursor(arrayOf(WordListContract.CONTENT_PATH))
        try {
            readableDB = readableDB ?: readableDatabase
            val count = DatabaseUtils.queryNumEntries(readableDB, WORD_LIST_TABLE)
            cursor.addRow(arrayOf(count))
        } catch (e: Exception) {
            Log.d(TAG, "EXCEPTION $e")
        }
        return cursor
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
        private const val DATABASE_VERSION = 1

        private val COLUMNS = arrayOf(KEY_ID, KEY_WORD)

        private const val WORD_LIST_TABLE_CREATE = "CREATE TABLE $WORD_LIST_TABLE ($KEY_ID INTEGER PRIMARY KEY, $KEY_WORD TEXT);"
        private const val WORD_LIST_TABLE_DROP = "DROP TABLE IF EXISTS $WORD_LIST_TABLE"
    }
}
