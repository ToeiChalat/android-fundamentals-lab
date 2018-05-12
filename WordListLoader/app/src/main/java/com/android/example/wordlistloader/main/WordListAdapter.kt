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

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.example.wordlistloader.R
import com.android.example.wordlistloader.data.WordListContract






/**
 * Implements a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
internal class WordListAdapter(mContext: Context)
    : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    private var cursor: Cursor? = null

    /**
     * Custom view holder with a text view and two buttons.
     */
    internal inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById<View>(R.id.word) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = mInflater.inflate(R.layout.wordlist_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        cursor?.let {
            if (it.moveToPosition(position)) {
                val indexWord = it.getColumnIndex(WordListContract.WordList.KEY_WORD)
                val word = it.getString(indexWord)
                holder.wordItemView.text = word
            } else {
                holder.wordItemView.setText(R.string.error_no_word)
            }
        } ?: Log.e(TAG, "onBindViewHolder: Cursor is null.")
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: -1
    }

    fun setData(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    companion object {
        val TAG = WordListAdapter::class.java.name
        const val EXTRA_ID = "ID"
        const val EXTRA_WORD = "WORD"
        const val EXTRA_POSITION = "POSITION"
    }
}


