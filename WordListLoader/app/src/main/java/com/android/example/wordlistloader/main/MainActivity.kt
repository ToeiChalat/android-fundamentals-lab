/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.wordlistloader.main

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.example.wordlistloader.R
import com.android.example.wordlistloader.data.WordListContract



/**
 * Implements a RecyclerView that displays a list of words from a SQL database.
 * - Clicking the fab button opens a second activity to add a word to the database.
 * - Clicking the Edit button opens an activity to edit the current word in the database.
 * - Clicking the Delete button deletes the current word from the database.
 */
class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private var recyclerView: RecyclerView? = null
    private var adapter: WordListAdapter? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val queryUri = WordListContract.CONTENT_URI.toString()
        val projection = arrayOf(WordListContract.CONTENT_PATH)
        return CursorLoader(
                this,
                Uri.parse(queryUri),
                projection,
                null,
                null,
                null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapter?.setData(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter?.setData(null)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportLoaderManager.initLoader(0, null, this)

        // Create recycler view.
        recyclerView = findViewById<View>(R.id.recyclerview) as RecyclerView
        // Create an adapter and supply the data to be displayed.
        adapter = WordListAdapter(this)
        // Connect the adapter with the recycler view.
        recyclerView?.adapter = adapter
        // Give the recycler view a default layout manager.
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

}