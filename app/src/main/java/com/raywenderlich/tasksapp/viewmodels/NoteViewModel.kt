package com.raywenderlich.tasksapp.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raywenderlich.tasksapp.repos.NoteRepository
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.NoteDatabase
import com.raywenderlich.tasksapp.fragments.AddNoteFragmentArgs
import kotlinx.coroutines.launch


class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val noteDB = NoteDatabase.getInstance(application)
    private var allNotes: LiveData<List<Note>>

    private val _navigateToAddFragment = MutableLiveData<Note>()
    val navigateToAddFragment : LiveData<Note>
    get() = _navigateToAddFragment


    private var selectedItemsCount: LiveData<Int>

    init {
        repository = NoteRepository(noteDB.noteDao())
        allNotes = repository.getAllNotes()
        selectedItemsCount = repository.getSelectedItemsCount()
    }

    fun getAllNotes() : LiveData<List<Note>>{
         return allNotes
    }
    fun getSelectedItemsCount() : LiveData<Int>{
        return selectedItemsCount
    }
    fun selectItem(note: Note) {
        viewModelScope.launch {
            repository.selectionItemState(note)
        }
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
    fun insertDataToDatabase(etTitle : EditText, etDescription : EditText, date : String, color : Int){
        viewModelScope.launch {
            repository.insertDataToDatabase(etTitle,etDescription,date,color)
        }
    }
    fun updateData(etTitle : EditText, etDescription : EditText,
                   date: String , args : AddNoteFragmentArgs, color: Int
    ){
        viewModelScope.launch {
            repository.updateData(etTitle,etDescription,date,args, color)
        }
    }

    fun unselectNotes() {
        viewModelScope.launch {
            repository.unselectNotes()
        }

    }
    fun selectAllNotes(){
        viewModelScope.launch {
            repository.selectAllNotes()
        }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            repository.deleteSelectedNotes()
        }
    }

}
