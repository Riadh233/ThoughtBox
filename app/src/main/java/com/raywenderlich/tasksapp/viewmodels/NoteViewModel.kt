package com.raywenderlich.tasksapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raywenderlich.tasksapp.NoteRepository
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository: NoteRepository
    private val noteDB = NoteDatabase.getInstance(application)
    private var allNotes: LiveData<List<Note>>

    private val _navigateToAddFragment = MutableLiveData<Note>()
    val navigateToAddFragment : LiveData<Note>
    get() = _navigateToAddFragment

    init {
        repository = NoteRepository(noteDB.dao())
        allNotes = repository.getAllNotes()
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            repository.deleteAllNotes()
        }
    }
    fun getAllNotes() : LiveData<List<Note>>{
         return allNotes
    }
    fun displayUpdateScreen(note : Note){
        _navigateToAddFragment.value = note
    }
    fun navigateToUpdateScreenFinished(){
        _navigateToAddFragment.value = null
    }
    fun searchDatabase(query : String) : LiveData<List<Note>>{
        return repository.searchDatabase(query)
    }
}
