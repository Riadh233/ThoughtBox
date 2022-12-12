package com.raywenderlich.tasksapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel(): ViewModel() {

    private val _deleteIconVisibility = MutableLiveData<Boolean>(false)
    val deleteIconVisibility: LiveData<Boolean>
    get() = _deleteIconVisibility

    private val _onDeleteEvent = MutableLiveData(false)
    val onDeleteEvent: LiveData<Boolean>
    get() = _onDeleteEvent





    fun showDeleteIcon(){
        _deleteIconVisibility.value = true
    }

    fun hideDeleteIcon(){
        _deleteIconVisibility.value = false
    }

    fun onDelete(){
        _onDeleteEvent.value = true
    }


    fun consumeDeletionEvent() {
        _onDeleteEvent.value = false
    }



}