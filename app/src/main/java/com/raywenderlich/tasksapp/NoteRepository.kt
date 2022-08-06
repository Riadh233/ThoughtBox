package com.raywenderlich.tasksapp

import androidx.lifecycle.LiveData
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.NoteDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext


class NoteRepository(private val dao: NoteDao) {

    suspend fun insert(note: Note) {
        withContext(IO) {
            dao.insert(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(IO) {
            dao.update(note)
        }
    }

    suspend fun delete(note: Note) {
        withContext(IO) {
            dao.delete(note)
        }
    }


    suspend fun deleteAllNotes() {
        withContext(IO) {
            dao.clear()
        }
    }
    fun searchDatabase(query : String) : LiveData<List<Note>>{
        return dao.searchDatabase(query)
    }

    fun getAllNotes() = dao.getAllNotes()
}