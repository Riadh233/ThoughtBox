package com.raywenderlich.tasksapp.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.tasksapp.data.Task
import com.raywenderlich.tasksapp.tools.AlarmUtils
import java.util.*

class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val _cabVisibility = MutableLiveData<Boolean>(false)
    val cabVisibility: LiveData<Boolean>
    get() = _cabVisibility

    private val _onDeleteNotesEvent = MutableLiveData(false)
    val onDeleteNotesEvent: LiveData<Boolean>
    get() = _onDeleteNotesEvent

    private val _onDeleteTasksEvent = MutableLiveData(false)
    val onDeleteTasksEvent: LiveData<Boolean>
        get() = _onDeleteTasksEvent

    private val _onCancelEvent = MutableLiveData(false)
    val onCancelEvent: LiveData<Boolean>
        get() = _onCancelEvent

    private val _selectedItemsCount = MutableLiveData(0)
    val selectedItemsCount : LiveData<Int>
    get() = _selectedItemsCount

    private val _onSelectAllNotesEvent = MutableLiveData(false)
    val onSelectAllNotes: LiveData<Boolean>
        get() = _onSelectAllNotesEvent

    private val _onSelectAllTasksEvent = MutableLiveData(false)
    val onSelectAllTasks: LiveData<Boolean>
        get() = _onSelectAllTasksEvent


    private val _navigateToTasksScreen = MutableLiveData(false)
    val navigateToTasksScreen: LiveData<Boolean>
        get() = _navigateToTasksScreen

    private val alarmUtils = AlarmUtils()


    fun showCAB(){
        _cabVisibility.value = true
    }

    fun hideCAB(){
        _cabVisibility.value = false
    }

    fun onDeleteNotes(){
        _onDeleteNotesEvent.value = true
    }
    fun onDeleteTasks(){
        _onDeleteTasksEvent.value = true
    }

    fun onCancel(){
        _onCancelEvent.value = true
    }


    fun consumeNotesDeletionEvent() {
        _onDeleteNotesEvent.value = false
    }
    fun consumeTasksDeletionEvent() {
        _onDeleteTasksEvent.value = false
    }

    fun consumeCancelEvent() {
        _onCancelEvent.value = false
    }
    fun setSelectedItemsCount(count : Int){
        _selectedItemsCount.value = count
    }

    fun selectAllNotesEvent(){
        _onSelectAllNotesEvent.value = true
    }

    fun consumeSelectAllNotesEvent(){
        _onSelectAllNotesEvent.value = false
    }
    fun selectAllTasksEvent(){
        _onSelectAllTasksEvent.value = true
    }

    fun consumeSelectAllTasksEvent(){
        _onSelectAllTasksEvent.value = false
    }
    fun navigateToTasksScreen(){
        _navigateToTasksScreen.value = true
    }
    fun navigateToNotesScreen(){
        _navigateToTasksScreen.value = false
    }
    fun setAlarm(taskId:Long,timeScheduled:Long){
        alarmUtils.setAlarm(taskId,timeScheduled,getApplication<Application>())
    }
    fun cancelAlarms(tasks : List<Task>){
        alarmUtils.cancelAlarms(tasks,getApplication<Application>())
    }
}