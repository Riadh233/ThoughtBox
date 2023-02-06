package com.raywenderlich.tasksapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {
    @Insert
    fun insert(task : Task)

    @Update
    fun update(task : Task)

    @Delete
    fun delete(task : Task)

    @Query("DELETE FROM tasks_table")
    fun clear()

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks() : LiveData<List<Task>>

//    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery ")
//    fun searchDatabase(searchQuery : String) : LiveData<List<Note>>
}