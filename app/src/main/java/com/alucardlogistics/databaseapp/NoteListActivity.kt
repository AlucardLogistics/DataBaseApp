package com.alucardlogistics.databaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alucardlogistics.databaseapp.adapters.NotesRecyclerAdapter
import com.alucardlogistics.databaseapp.models.Note
import com.alucardlogistics.databaseapp.util.VerticalSpacingItemDecorator
import com.alucardlogistics.databaseapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note_list.*

class NoteListActivity : AppCompatActivity(),
    NotesRecyclerAdapter.OnNoteListener,
    View.OnClickListener {

    private var mNotes = arrayListOf<Note>()
    private lateinit var adapter: NotesRecyclerAdapter
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        initRecyclerView()

        fab.setOnClickListener(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        //populateList()

        noteViewModel.allNotes.observe(this, Observer {
            it?.let {
                adapter.setNotes(it)
                mNotes.clear()
                mNotes.addAll(it)
                Log.d("OUTOFBOUND", "inside Observer size after setNotes " + mNotes.size)
            }
        })

        setSupportActionBar(findViewById(R.id.main_toolbar))
        title = "Notes" //setting toolbar title

    }

    private fun populateList() {
        for (i in 0..100) {
            val note = Note(i, "title $i", "content for Note no. $i", "Jan Year: $i")
            mNotes.add(note)
        }
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        adapter = NotesRecyclerAdapter(this, mNotes, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(VerticalSpacingItemDecorator(10))
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view)
        recycler_view.adapter = adapter
        Log.d("OUTOFBOUND", "initRecyclerView " + mNotes.size)
    }


    override fun onNoteClick(position: Int) {
        val openNoteIntent = Intent(this, NoteActivity::class.java)
        Log.d("OUTOFBOUND", "onNoteClick size " + mNotes.size)
        Log.d("OUTOFBOUND", "onNoteClick position " + mNotes[position])
        openNoteIntent.putExtra(SELECTED_NOTE, mNotes[position])
        startActivity(openNoteIntent)

    }

    override fun onClick(v: View?) {
        startActivity(Intent(this, NoteActivity::class.java))
    }

    private fun deleteNote(note: Note) {
        //mNotes.remove(note)
        noteViewModel.deleteNote(note)
        adapter.notifyDataSetChanged()
    }

    private val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val noteTitle = mNotes[viewHolder.adapterPosition].title
                Toast.makeText(applicationContext, "$noteTitle has been deleted",Toast.LENGTH_SHORT).show()
                deleteNote(mNotes[viewHolder.adapterPosition])
            }

        }

    companion object {
        private const val SELECTED_NOTE = "SELECTED_NOTE"
    }
}
