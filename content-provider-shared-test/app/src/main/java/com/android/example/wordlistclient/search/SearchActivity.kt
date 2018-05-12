package com.android.example.wordlistclient.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.example.wordlistclient.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    fun showResult(view: View) {
        val word = searchWord.text.toString()
        searchResult.text = "Search term: $word:\n\n"

//        val cursor = db.search(word)
//        if (cursor != null && cursor.count > 0) {
//            cursor.moveToFirst()
//            var index: Int
//            var result: String
//            do {
//                index = cursor.getColumnIndex(WordListContract.WordList.KEY_WORD)
//                result = cursor.getString(index)
//                searchResult.append("$result\n")
//            } while (cursor.moveToNext())
//            cursor.close()
//        }
    }
}
