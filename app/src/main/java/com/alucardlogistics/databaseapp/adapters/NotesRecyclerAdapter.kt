package com.alucardlogistics.databaseapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alucardlogistics.databaseapp.R
import com.alucardlogistics.databaseapp.models.Note
import com.alucardlogistics.databaseapp.util.Utility
import java.lang.NullPointerException

class NotesRecyclerAdapter internal constructor(
    context: Context,
    private var mNotes: List<Note>,
    private var onNoteListener: OnNoteListener
): RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder>() {

    interface OnNoteListener {
        fun onNoteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list_item, parent, false)

        return ViewHolder(view, onNoteListener)
    }

    override fun getItemCount() = mNotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            var month = mNotes[position].timeStamp?.substring(0, 2)
            month = Utility.getMonthFromNumber(month)
            var year = mNotes[position].timeStamp?.substring(3)
            var timeStamp = "$month $year"

            holder.timeStamp.text = timeStamp
            holder.title.text = mNotes[position].title
        } catch (e: NullPointerException) {
            Log.e("NotesRecyclerAdapter", "NullPointerException: " + e.message)
        }
    }

    fun setNotes(notes: List<Note>) {
        this.mNotes = notes
        notifyDataSetChanged()
    }

    class ViewHolder(
        itemView: View,
        private var onNoteListener: OnNoteListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        val title: TextView = itemView.findViewById(R.id.note_title)
        val timeStamp: TextView = itemView.findViewById(R.id.note_time_stamp)

        override fun onClick(v: View?) {
            onNoteListener.onNoteClick(adapterPosition)

        }
    }
}