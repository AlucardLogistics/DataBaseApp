package com.alucardlogistics.databaseapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alucardlogistics.databaseapp.models.Note
import com.alucardlogistics.databaseapp.util.Utility
import com.alucardlogistics.databaseapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_note_toolbar.*

class NoteActivity : AppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    View.OnClickListener,
    TextWatcher {

    private var initialNewNote = Note()
    private var finalNote = Note()
    private lateinit var noteViewModel: NoteViewModel
    private var isNewNote: Boolean = false
    private var mMode: Int = -1

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        if (getIncomingIntent()) {
            setNewNoteProperties()
            enableEditMode()
        } else {
            setNoteProperties()
            disableContentInteraction()
        }

        setListeners()
    }

    private fun getIncomingIntent(): Boolean {
        if (intent.hasExtra(SELECTED_NOTE)) {
            initialNewNote = intent.getParcelableExtra(SELECTED_NOTE)

            finalNote = Note()
            finalNote.title = initialNewNote.title
            finalNote.content = initialNewNote.content
            finalNote.timeStamp = initialNewNote.timeStamp
            finalNote.id = initialNewNote.id

            mMode = EDITE_MODE_DISABLED
            isNewNote = false
            return false
        }

        mMode = EDITE_MODE_ENABLED
        isNewNote = true
        Log.d("NOTE", "not new Note")
        return true
    }

    private fun saveNewNote() {
        noteViewModel.insertNotes(finalNote)
    }

    private fun saveChanges() {
        if(isNewNote) {
            saveNewNote()
        } else {
            updateNote()
        }
    }

    private fun updateNote() {
        noteViewModel.updateNote(finalNote)
    }

    private fun enableEditMode() {
        back_arrow_container.visibility = View.GONE
        check_container.visibility = View.VISIBLE

        note_text_title.visibility = View.GONE
        note_edit_title.visibility = View.VISIBLE

        mMode = EDITE_MODE_ENABLED

        enableContentInteraction()
    }

    private fun disableEditMode() {
        back_arrow_container.visibility = View.VISIBLE
        check_container.visibility = View.GONE

        note_text_title.visibility = View.VISIBLE
        note_edit_title.visibility = View.GONE

        mMode = EDITE_MODE_DISABLED

        disableContentInteraction()

        var temp = note_text.text.toString()
        temp = temp.replace("\n", "")
        temp = temp.replace(" ", "")
        if(temp.isNotEmpty()) {
            finalNote.title = note_edit_title.text.toString()
            finalNote.content = note_text.text.toString()
            finalNote.timeStamp = Utility.currentTimeStamp

            if(!finalNote.content.equals(initialNewNote.content) ||
                !finalNote.title.equals(initialNewNote.title)) {
                saveChanges()
            }
        }
    }

    private fun enableContentInteraction() {
        note_text.keyListener = EditText(this).keyListener
        note_text.isFocusable = true
        note_text.isFocusableInTouchMode = true
        note_text.isCursorVisible = true
        note_text.requestFocus()
    }

    private fun disableContentInteraction() {
        note_text.keyListener = null
        note_text.isFocusable = false
        note_text.isFocusableInTouchMode = false
        note_text.isCursorVisible = false
        note_text.clearFocus()
    }

    private fun setNoteProperties() {
        note_text_title.text = initialNewNote.title
        note_edit_title.setText(initialNewNote.title)
        note_text.setText(initialNewNote.content)
    }

    private fun setNewNoteProperties() {
        note_text_title.text = getString(R.string.note_title)
        note_edit_title.setText(getString(R.string.note_title))

        initialNewNote = Note()
        finalNote = Note()


        initialNewNote.title = getString(R.string.note_title)
        finalNote.title = getString(R.string.note_title)
    }

    private fun setListeners() {
        note_text.setOnTouchListener(this)
        gestureDetector = GestureDetector(this, this)
        note_text_title.setOnClickListener(this)
        toolbar_check.setOnClickListener(this)
        toolbar_back_arrow.setOnClickListener(this)
        note_edit_title.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            toolbar_check -> {
                hideKeyboard()
                disableEditMode()
            }
            note_text_title -> {
                enableEditMode()
                note_edit_title.requestFocus()
                note_edit_title.setSelection(note_edit_title.text.length)
            }
            toolbar_back_arrow -> finish()
        }
    }

    override fun onBackPressed() {
        if(mMode == EDITE_MODE_ENABLED) {
            onClick(toolbar_check)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mode", mMode)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mMode = savedInstanceState.getInt("mode")
        if(mMode == EDITE_MODE_ENABLED) {
            enableEditMode()
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        note_text_title.text = s.toString()
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.d("TOUCH ME", "double tapped")
        enableEditMode()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    companion object {
        private const val SELECTED_NOTE = "SELECTED_NOTE"
        private const val EDITE_MODE_ENABLED = 1
        private const val EDITE_MODE_DISABLED = 0
    }
}
