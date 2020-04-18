package com.alucardlogistics.databaseapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alucardlogistics.databaseapp.models.Note


@Dao
interface NoteDao {

    @Insert
    suspend fun insertNotes(vararg notes: Note)

    @Query("SELECT * FROM notes_table")
    fun getNotes(): LiveData<List<Note>>

    @Delete
    suspend fun deleteNotes(vararg notes: Note)

    @Update
    suspend fun updateNotes(vararg notes: Note)

}