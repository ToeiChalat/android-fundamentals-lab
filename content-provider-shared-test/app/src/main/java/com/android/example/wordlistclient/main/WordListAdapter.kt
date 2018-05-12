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

package com.android.example.wordlistclient.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.example.wordlistclient.edit.EditWordActivity
import com.android.example.wordlistclient.R
import com.android.example.wordlistclient.data.WordListContract




/**
 * Implements a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
internal class WordListAdapter(internal var mContext: Context)
    : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    private val queryUri = WordListContract.CONTENT_URI.toString() // base uri
    private val projection = arrayOf(WordListContract.CONTENT_PATH) //table
    private val selectionClause: String? = null
    private val selectionArgs: Array<String>? = null
    private val sortOrder = "ASC"

    /**
     * Custom view holder with a text view and two buttons.
     */
    internal inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById<View>(R.id.word) as TextView
        var deleteButton: Button = itemView.findViewById<View>(R.id.delete_button) as Button
        var editButton: Button = itemView.findViewById<View>(R.id.edit_button) as Button

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = mInflater.inflate(R.layout.wordlist_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val cursor = mContext.contentResolver.query(
                Uri.parse(queryUri),
                null,
                null,
                null,
                sortOrder
        )
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                val indexWord = cursor.getColumnIndex(WordListContract.WordList.KEY_WORD)
                val word = cursor.getString(indexWord)
                val indexId = cursor.getColumnIndex(WordListContract.WordList.KEY_ID)
                val id = cursor.getInt(indexId)
                holder.wordItemView.text = word
                holder.deleteButton.setOnClickListener(createOnDeleteClickListener(id, word, holder))
                holder.editButton.setOnClickListener(createEditClickListener(id, word, holder))
            } else {
                holder.wordItemView.setText(R.string.error_no_word)
            }
        }
        cursor.close()
    }

    private fun createOnDeleteClickListener(id: Int, word: String, holder: WordViewHolder): MyButtonOnClickListener {
        return object : MyButtonOnClickListener(id, word) {
            override fun onClick(v: View) {
                val deleted = mContext.contentResolver.delete(
                        WordListContract.CONTENT_URI,
                        WordListContract.CONTENT_PATH,
                        arrayOf(id.toString())
                )
                if (deleted >= 0) {
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, itemCount)
                }
            }
        }
    }

    private fun createEditClickListener(id: Int, word: String, holder: WordViewHolder): MyButtonOnClickListener {
        return object : MyButtonOnClickListener(id, word) {
            override fun onClick(v: View) {
                val intent = Intent(mContext, EditWordActivity::class.java)

                intent.putExtra(EXTRA_ID, id)
                intent.putExtra(EXTRA_POSITION, holder.adapterPosition)
                intent.putExtra(EXTRA_WORD, word)

                (mContext as Activity).startActivityForResult(
                        intent, MainActivity.WORD_EDIT)
            }
        }
    }

    override fun getItemCount(): Int {
        val cursor = mContext.contentResolver.query(
                WordListContract.ROW_COUNT_URI,
                arrayOf("count(*) AS count"),
                selectionClause,
                selectionArgs,
                sortOrder
        )
        return try {
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            cursor.close()
            count
        } catch (e: Exception) {
            Log.d(TAG, "EXCEPTION getItemCount: $e")
            -1
        }
    }

    companion object {
        val TAG = WordListAdapter::class.java.name
        const val EXTRA_ID = "ID"
        const val EXTRA_WORD = "WORD"
        const val EXTRA_POSITION = "POSITION"
    }
}


