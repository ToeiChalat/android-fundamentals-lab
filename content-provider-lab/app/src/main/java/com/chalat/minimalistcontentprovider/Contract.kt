package com.chalat.minimalistcontentprovider

import android.net.Uri

/**
 *
 * Created by Chalat Chansima on 5/12/18.
 *
 */
object Contract {

    const val SINGLE_RECORD_MEME_TYPE = "vnd.android.cursor.item/vnd.com.example.provider.words"
    const val MULTIPLE_RECORD_MEME_TYPE = "vnd.android.cursor.dir/vnd.com.example.provider.words"
    const val AUTHORITY = "com.android.example.minimalistcontentprovider.provider"
    const val CONTENT_PATH = "words"
    const val CONTENT_URI = "content://$AUTHORITY/$CONTENT_PATH"
    const val ALL_ITEMS = -1
    const val WORD_ID = "id"
}