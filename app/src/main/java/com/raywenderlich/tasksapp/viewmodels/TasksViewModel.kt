package com.raywenderlich.tasksapp.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.raywenderlich.tasksapp.repos.TasksRepository
import com.raywenderlich.tasksapp.data.NoteDatabase
import com.raywenderlich.tasksapp.data.Task
import kotlinx.coroutines.*
import java.util.*

class TasksViewModel(application : Application) : AndroidViewModel(application) {
    private val repository: TasksRepository
    private val appDB = NoteDatabase.getInstance(application)
    private var allTasks: LiveData<List<Task>>
    private var selectedTasks : LiveData<List<Task>>
    private var selectedItemsCount: LiveData<Int>


    private val _navigateToAddFragment = MutableLiveData<Task>()
    val navigateToAddFragment : LiveData<Task>
        get() = _navigateToAddFragment


    init {
        repository = TasksRepository(appDB.taskDao())
        allTasks = repository.getAllTasks()
        selectedTasks = repository.getSelectedTasks()
        selectedItemsCount = repository.getSelectedItemsCount()
    }
    fun insetTask(task : Task){
        viewModelScope.launch {
            repository.insert(task)
        }
    }
    fun updateTask(task : Task){
        viewModelScope.launch {
            repository.update(task)
        }
    }
    fun deleteTask(task : Task){
        viewModelScope.launch {
            repository.delete(task)
        }
    }
    fun deleteAllTasks(){
        viewModelScope.launch {
            repository.deleteAllTasks()
        }
    }
    fun getAllTasks() : LiveData<List<Task>>{
        return allTasks
    }
    fun getSelectedTasks() : LiveData<List<Task>>{
        return selectedTasks
    }

    fun displayUpdateScreen(task : Task){
        _navigateToAddFragment.value = task
    }
    fun navigateToUpdateScreenFinished(){
        _navigateToAddFragment.value = null
    }
    fun insertDataToDatabase(id:Long,etTitle: TextInputEditText, etDescription: TextInputEditText, taskPriority: Int, time: String) {
        viewModelScope.launch {
            repository.insertDataToDatabase(id,etTitle,etDescription,taskPriority,time)
        }

    }
    fun updateData(id:Long,etTitle: TextInputEditText, etDescription: TextInputEditText, taskPriority: Int, time: String){
        viewModelScope.launch {
            repository.updateData(id,etTitle,etDescription,taskPriority,time)
        }
    }
    fun searchDatabase(query : String) : LiveData<List<Task>>{
        return repository.searchDatabase(query)
    }
    fun unselectTasks() {
        viewModelScope.launch {
            repository.unselectTasks()
        }

    }
    fun selectAllTasks(){
        viewModelScope.launch {
            repository.selectAllTasks()
        }
    }

    fun deleteSelectedTasks() {
        viewModelScope.launch {
            repository.deleteSelectedTasks()
        }
    }
    fun getSelectedItemsCount() : LiveData<Int>{
        return selectedItemsCount
    }
    fun selectItem(task: Task) {
        viewModelScope.launch {
            repository.selectionItemState(task)
        }
    }
    fun getTaskById(taskId : Long) : Task?{
        var task : Task ?= null
        viewModelScope.launch {
            task = repository.getTaskById(taskId)
        }
        return task
    }
    fun changeCheckState(task : Task){
        viewModelScope.launch {
            repository.changeCheckState(task)
        }
    }
}