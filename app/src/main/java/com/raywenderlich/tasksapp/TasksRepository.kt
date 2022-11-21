package com.raywenderlich.tasksapp

import androidx.lifecycle.LiveData
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.Task
import com.raywenderlich.tasksapp.data.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksRepository(private val dao : TasksDao) {
    suspend fun insert(task: Task) {
        withContext(Dispatchers.IO) {
            dao.insert(task)
        }
    }

    suspend fun update(note: Task) {
        withContext(Dispatchers.IO) {
            dao.update(note)
        }
    }

    suspend fun delete(note: Task) {
        withContext(Dispatchers.IO) {
            dao.delete(note)
        }
    }


    suspend fun deleteAllTasks() {
        withContext(Dispatchers.IO) {
            dao.clear()
        }
    }

    fun getAllTasks() = dao.getAllTasks()

}