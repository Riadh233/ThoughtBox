package com.raywenderlich.tasksapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun clear()

    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery ")
    fun searchDatabase(searchQuery: String): LiveData<List<Note>>

    @Query("SELECT COUNT(*) FROM note_table WHERE selected=1")
    fun getAllSelectedNotes(): LiveData<Int>

    @Query("UPDATE note_table SET selected = 0 WHERE id = :id")
    fun unselectNote(id : Int)

    @Query("UPDATE note_table SET selected = 1 WHERE id = :id")
    fun selectNote(id : Int)

    @Query("UPDATE note_table SET selected = 0")
    fun unselectAllNotes(): Int

    @Query("DELETE FROM note_table WHERE selected=1")
    fun deleteSelectedNotes(): Int
}
