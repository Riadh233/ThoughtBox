package com.raywenderlich.tasksapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raywenderlich.tasksapp.TasksRepository
import com.raywenderlich.tasksapp.data.NoteDatabase
import com.raywenderlich.tasksapp.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TasksViewModel(application : Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository: TasksRepository
    private val appDB = NoteDatabase.getInstance(application)
    private var allTasks: LiveData<List<Task>>

    private val _navigateToAddFragment = MutableLiveData<Task>()
    val navigateToAddFragment : LiveData<Task>
        get() = _navigateToAddFragment

    init {
        repository = TasksRepository(appDB.taskDao())
        allTasks = repository.getAllTasks()
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
    fun displayUpdateScreen(task : Task){
        _navigateToAddFragment.value = task
    }
    fun navigateToUpdateScreenFinished(){
        _navigateToAddFragment.value = null
    }



}