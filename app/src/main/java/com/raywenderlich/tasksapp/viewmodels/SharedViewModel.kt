package com.raywenderlich.tasksapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel(): ViewModel() {

    private val _cabVisibility = MutableLiveData<Boolean>(false)
    val cabVisibility: LiveData<Boolean>
    get() = _cabVisibility

    private val _onDeleteEvent = MutableLiveData(false)
    val onDeleteEvent: LiveData<Boolean>
    get() = _onDeleteEvent

    private val _onCancelEvent = MutableLiveData(false)
    val onCancelEvent: LiveData<Boolean>
        get() = _onCancelEvent

    private val _selectedItemsCount = MutableLiveData(0)
    val selectedItemsCount : LiveData<Int>
    get() = _selectedItemsCount

    private val _onSelectAllEvent = MutableLiveData(false)
    val onSelectAll: LiveData<Boolean>
        get() = _onSelectAllEvent


    fun showCAB(){
        _cabVisibility.value = true
    }

    fun hideCAB(){
        _cabVisibility.value = false
    }

    fun onDelete(){
        _onDeleteEvent.value = true
    }

    fun onCancel(){
        _onCancelEvent.value = true
    }


    fun consumeDeletionEvent() {
        _onDeleteEvent.value = false
    }

    fun consumeCancelEvent() {
        _onCancelEvent.value = false
    }
    fun setSelectedItemsCount(count : Int){
        _selectedItemsCount.value = count
    }

    fun selectAllEvent(){
        _onSelectAllEvent.value = true
    }

    fun consumeSelectAllEvent(){
        _onSelectAllEvent.value = false
    }
}