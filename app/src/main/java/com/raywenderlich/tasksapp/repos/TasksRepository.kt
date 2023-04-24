package com.raywenderlich.tasksapp.repos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.android.material.textfield.TextInputEditText
import com.raywenderlich.tasksapp.data.Task
import com.raywenderlich.tasksapp.data.TasksDao
import com.raywenderlich.tasksapp.tools.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TasksRepository(private val dao : TasksDao) {


    suspend fun insert(task: Task) {
        withContext(Dispatchers.IO) {
            dao.insert(task)
        }
    }

    suspend fun update(task: Task) {
        withContext(Dispatchers.IO) {
            dao.update(task)
        }
    }

    suspend fun delete(task: Task) {
        withContext(Dispatchers.IO) {
            dao.delete(task)
        }
    }


    suspend fun deleteAllTasks() {
        withContext(Dispatchers.IO) {
            dao.clear()
        }
    }
    suspend fun insertDataToDatabase(id:Long,etTitle: TextInputEditText, etDescription: TextInputEditText, taskPriority: Int, time: String) {
        withContext(Dispatchers.IO){
            dao.insert(Task(id,etTitle.text.toString(),etDescription.text.toString(),taskPriority,time))
        }
    }
    fun searchDatabase(query : String) : LiveData<List<Task>>{
        return dao.searchDatabase(query)
    }

    fun getAllTasks() = dao.getAllTasks()
    fun getSelectedTasks() = dao.getSelectedTasks()
    fun getSelectedItemsCount() :LiveData<Int> = dao.getAllSelectedTasks()

    suspend fun getTaskById(id : Long) : Task?{
        Log.d("getTaskById","used")
       return  withContext(Dispatchers.IO){
            dao.getTaskById(id)
        }
    }
    suspend fun unselectTasks() {
        withContext(Dispatchers.IO){
            dao.unselectAllTasks()
        }
    }
    suspend fun selectAllTasks(){
        withContext(Dispatchers.IO){
            dao.selectAllTasks()
        }
    }
    suspend fun deleteSelectedTasks() {
        withContext(Dispatchers.IO){
            dao.deleteSelectedTasks()
        }
        Log.d("deletionEvent","clicked")
    }
    suspend fun selectionItemState(task: Task){
        withContext(Dispatchers.IO){
            if(task.selected)
                dao.unselectTask(task.id)
            else
                dao.selectTask(task.id)
        }
    }
    suspend fun updateData(id:Long,etTitle: TextInputEditText, etDescription: TextInputEditText, taskPriority: Int, time: String){
        withContext(Dispatchers.IO){
            dao.update(Task(id,etTitle.text.toString(),etDescription.text.toString(),taskPriority,time))
        }
    }
    suspend fun updateAlarmText(id : Long){
        withContext(Dispatchers.IO){
            dao.updateAlarmText(id,"Expired")
        }
        Log.d("update text","done")
    }

}