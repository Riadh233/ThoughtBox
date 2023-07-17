package com.raywenderlich.tasksapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raywenderlich.tasksapp.repos.TasksRepository
import com.raywenderlich.tasksapp.data.NoteDatabase
import com.raywenderlich.tasksapp.data.Task
import kotlinx.coroutines.*

class TasksViewModel(application : Application) : AndroidViewModel(application) {
    private val repository: TasksRepository
    private val appDB = NoteDatabase.getInstance(application)
    private var allTasks: LiveData<List<Task>>
    private var selectedTasks : LiveData<List<Task>>
    private var selectedItemsCount: LiveData<Int>


    private val _navigateToAddFragment = MutableLiveData<Task?>()
    val navigateToAddFragment : LiveData<Task?>
        get() = _navigateToAddFragment


    init {
        repository = TasksRepository(appDB.taskDao())
        allTasks = repository.getAllTasks()
        selectedTasks = repository.getSelectedTasks()
        selectedItemsCount = repository.getSelectedItemsCount()
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
    fun insertDataToDatabase(
        id:Long,
        title: String, description: String, taskPriority: Int, time: String,
        scheduledDate: String
    ) {
        viewModelScope.launch {
            repository.insertDataToDatabase(id,title,description,taskPriority,time,scheduledDate)
        }

    }
    fun updateData(id:Long,title: String, description: String, taskPriority: Int, time: String,scheduledDate: String){
        viewModelScope.launch {
            repository.updateData(id,title,description,taskPriority,time,scheduledDate)
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
    fun changeCheckState(task : Task){
        viewModelScope.launch {
            repository.changeCheckState(task)
        }
    }
}