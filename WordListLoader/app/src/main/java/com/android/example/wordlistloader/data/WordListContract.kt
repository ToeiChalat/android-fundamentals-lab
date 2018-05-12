package com.android.example.wordlistloader.data

import android.net.Uri
import android.provider.BaseColumns

/**
 *
 * Created by Chalat Chansima on 5/12/18.
 *
 */
object WordListContract {

    const val DATABASE_NAME = "com.chalat.wordlist.database"

    const val ALL_ITEMS = -2
    const val COUNT = "count"

    const val AUTHORITY = "com.chalat.wordlistsqlwithcontentprovider.provider"
    const val CONTENT_PATH = "words"

    val CONTENT_URI = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")!!
    val ROW_COUNT_URI = Uri.parse("content://$AUTHORITY/$CONTENT_PATH/$COUNT")!!

    val SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.chalat.provider.words"
    val MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.item/vnd.com.chalat.provider.words"


    object WordList : BaseColumns {
        const val WORD_LIST_TABLE = "word_entries"
        const val KEY_ID = "_id"
        const val KEY_WORD = "word"
    }

}