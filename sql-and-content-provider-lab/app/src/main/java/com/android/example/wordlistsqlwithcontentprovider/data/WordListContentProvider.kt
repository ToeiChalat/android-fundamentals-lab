package com.android.example.wordlistsqlwithcontentprovider.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.content.UriMatcher
import android.util.Log
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.ALL_ITEMS
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.MULTIPLE_RECORDS_MIME_TYPE
import com.android.example.wordlistsqlwithcontentprovider.data.WordListContract.SINGLE_RECORD_MIME_TYPE


class WordListContentProvider : ContentProvider() {

    private var db: WordListOpenHelper? = null

    override fun delete(uri: Uri, selection: String, selectionArgs: Array<String>): Int {
        context.contentResolver.notifyChange(WordListContract.CONTENT_URI, null)
        return db?.delete(selectionArgs[0].toInt()) ?: -1
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            URI_ALL_ITEMS_CODE -> MULTIPLE_RECORDS_MIME_TYPE
            URI_ONE_ITEM_CODE -> SINGLE_RECORD_MIME_TYPE
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val id = db?.insert(values)
        context.contentResolver.notifyChange(WordListContract.CONTENT_URI, null)
        return Uri.parse("${WordListContract.CONTENT_PATH}/$id")
    }

    override fun onCreate(): Boolean {
        db = WordListOpenHelper(context)
        initializeUriMatching()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var cursor: Cursor? = null

        when (uriMatcher.match(uri)) {
            URI_ALL_ITEMS_CODE -> cursor = db?.query(ALL_ITEMS)
            URI_ONE_ITEM_CODE -> cursor = db?.query(uri.lastPathSegment.toInt())
            URI_COUNT_CODE -> cursor = db?.count()
            UriMatcher.NO_MATCH ->
                // You should do some error handling here.
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME: $uri")
            else ->
                // You should do some error handling here.
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED: $uri")
        }
        cursor?.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun update(uri: Uri,
                        values: ContentValues,
                        selection: String,
                        selectionArgs: Array<String>): Int {
        context.contentResolver.notifyChange(WordListContract.CONTENT_URI, null)
        return db?.update(
                selectionArgs[0].toInt(),
                values.getAsString(WordListContract.WordList.KEY_WORD)
        ) ?: -2
    }

    private fun initializeUriMatching() {
        uriMatcher.addURI(WordListContract.AUTHORITY, WordListContract.CONTENT_PATH, URI_ALL_ITEMS_CODE)
        uriMatcher.addURI(
                WordListContract.AUTHORITY,
                "${WordListContract.CONTENT_PATH}/#",
                URI_ALL_ITEMS_CODE
        )
        uriMatcher.addURI(
                WordListContract.AUTHORITY,
                "${WordListContract.CONTENT_PATH}/${WordListContract.COUNT}",
                URI_COUNT_CODE
        )
    }

    companion object {
        private val TAG = WordListContentProvider::class.java.name
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private const val URI_ALL_ITEMS_CODE = 10
        private const val URI_ONE_ITEM_CODE = 20
        private const val URI_COUNT_CODE = 30
    }
}
