package com.alucardlogistics.databaseapp.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update
import com.alucardlogistics.databaseapp.models.Note

class NoteRepository(var noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNotes(note)
    }

    @Update
    suspend fun updateNote(note: Note) {
        noteDao.updateNotes(note)
    }

    @Delete
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNotes(note)
    }







}