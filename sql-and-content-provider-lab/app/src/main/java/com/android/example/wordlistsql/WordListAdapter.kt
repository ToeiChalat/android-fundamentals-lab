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
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.android.example.wordlistsql.db.WordListOpenHelper

/**
 * Implements a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
internal class WordListAdapter(internal var mContext: Context, private val mDB: WordListOpenHelper?)
    : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

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
        val wordItem = mDB?.query(position)
        if (wordItem != null) {
            val word = wordItem.wordValue
            val id = wordItem.wordId
            holder.wordItemView.text = word
            holder.deleteButton.setOnClickListener(object : MyButtonOnClickListener(id, word) {
                override fun onClick(v: View) {
                    val deleted = mDB?.delete(id)
                    if (deleted != null && deleted >= 0)
                        notifyItemRemoved(holder.adapterPosition)
                }
            })
            holder.editButton.setOnClickListener(object : MyButtonOnClickListener(id, word) {
                override fun onClick(v: View) {
                    val intent = Intent(mContext, EditWordActivity::class.java)

                    intent.putExtra(EXTRA_ID, id)
                    intent.putExtra(EXTRA_POSITION, holder.adapterPosition)
                    intent.putExtra(EXTRA_WORD, word)

                    (mContext as Activity).startActivityForResult(
                            intent, MainActivity.WORD_EDIT)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return mDB?.count()?.toInt() ?: 0
    }

    companion object {
        const val EXTRA_ID = "ID"
        const val EXTRA_WORD = "WORD"
        const val EXTRA_POSITION = "POSITION"
    }
}


