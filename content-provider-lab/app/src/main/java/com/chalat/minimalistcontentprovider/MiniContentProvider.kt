package com.chalat.minimalistcontentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

/**
 *
 * Created by Chalat Chansima on 5/12/18.
 *
 */
class MiniContentProvider : ContentProvider() {

    var data: Array<String>? = null

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        Log.e(TAG, "Not implemented: insert uri: " + uri.toString())
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri,
                       projection: Array<out String>?,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?
    ): Cursor {
        var id = -1
        when (uriMatcher.match(uri)) {
            RESULT_CODE_ALL -> {
                if (selection != null) {
                    id = selectionArgs?.get(0)?.toInt() ?: -1
                }
            }
            RESULT_CODE_FIRST -> {
                id = uri.lastPathSegment.toInt()
            }
            UriMatcher.NO_MATCH -> {
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME.")
            }
            else -> {
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED.")
            }
        }
        Log.d(TAG, "query: $id")
        return populateCursor(id)
    }

    override fun onCreate(): Boolean {
        initializeUriMatcher()
        data = context?.resources?.getStringArray(R.array.words)
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.e(TAG, "Not implemented: update uri: " + uri.toString())
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.e(TAG, "Not implemented: deleted uri: " + uri.toString())
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String? {
        return when (uriMatcher.match(uri)) {
            RESULT_CODE_FIRST -> {
                Contract.SINGLE_RECORD_MEME_TYPE
            }
            RESULT_CODE_ALL -> {
                Contract.MULTIPLE_RECORD_MEME_TYPE
            }
            else -> {
                null
            }
        }
    }

    private fun populateCursor(id: Int): Cursor {
        val matrixCursor = MatrixCursor(arrayOf(Contract.CONTENT_PATH))
        when {
            id == Contract.ALL_ITEMS -> {
                data?.forEach {word ->
                    matrixCursor.addRow(arrayOf(word))
                }
            }
            id >= RESULT_CODE_ALL -> {
                data?.let {
                    matrixCursor.addRow(arrayOf(it[id]))
                }
            }
        }
        return matrixCursor
    }

    private fun initializeUriMatcher() {
        uriMatcher.addURI(Contract.AUTHORITY, "${Contract.CONTENT_PATH}/#", RESULT_CODE_FIRST)
        uriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, RESULT_CODE_ALL)
    }

    companion object {
        private val TAG = MiniContentProvider::class.java.name
        private const val RESULT_CODE_FIRST = 1
        private const val RESULT_CODE_ALL = 0
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }
}
