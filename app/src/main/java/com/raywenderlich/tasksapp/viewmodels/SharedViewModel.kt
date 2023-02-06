package com.raywenderlich.tasksapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel(): ViewModel() {

    private val _deleteAndCancelIconVisibility = MutableLiveData<Boolean>(false)
    val deleteAndCancelIconVisibility: LiveData<Boolean>
    get() = _deleteAndCancelIconVisibility

    private val _onDeleteEvent = MutableLiveData(false)
    val onDeleteEvent: LiveData<Boolean>
    get() = _onDeleteEvent



    fun showDeleteAndCancelIcon(){
        _deleteAndCancelIconVisibility.value = true
    }

    fun hideDeleteAndCancelIcon(){
        _deleteAndCancelIconVisibility.value = false
    }

    fun onDelete(){
        _onDeleteEvent.value = true
    }


    fun consumeDeletionEvent() {
        _onDeleteEvent.value = false
    }

}