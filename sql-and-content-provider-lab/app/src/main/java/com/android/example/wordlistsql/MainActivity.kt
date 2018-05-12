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

package com.android.example.wordlistsql

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.android.example.wordlistsql.db.WordListOpenHelper

/**
 * Implements a RecyclerView that displays a list of words from a SQL database.
 * - Clicking the fab button opens a second activity to add a word to the database.
 * - Clicking the Edit button opens an activity to edit the current word in the database.
 * - Clicking the Delete button deletes the current word from the database.
 */
class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var adapter: WordListAdapter? = null

    private var mDB: WordListOpenHelper? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mDB = WordListOpenHelper(this)

        // Create recycler view.
        recyclerView = findViewById<View>(R.id.recyclerview) as RecyclerView
        // Create an adapter and supply the data to be displayed.
        adapter = WordListAdapter(this, mDB)
        // Connect the adapter with the recycler view.
        recyclerView?.adapter = adapter
        // Give the recycler view a default layout manager.
        recyclerView?.layoutManager = LinearLayoutManager(this)

        // Add a floating action click handler for creating new entries.
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            // Start empty edit activity.
            val intent = Intent(baseContext, EditWordActivity::class.java)
            startActivityForResult(intent, WORD_EDIT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WORD_EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                val word = data.getStringExtra(EditWordActivity.EXTRA_REPLY)
                if (!word.isEmpty()) {
                    val id = data.getIntExtra(WordListAdapter.EXTRA_ID, -99)
                    if (id == WORD_ADD) {
                        mDB?.insert(word)
                    } else if (id >= 0) {
                        mDB?.update(id, word)
                    }

                    adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this,
                            R.string.empty_not_saved,
                            Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    companion object {
        const val WORD_EDIT = 1
        const val WORD_ADD = -1
    }
}