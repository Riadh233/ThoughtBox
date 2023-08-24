package com.notesapp.thoughtbox.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {
    @Insert
    fun insert(task : Task)

    @Update
    fun update(task : Task)

    @Query("UPDATE tasks_table SET alarmTime = :newText WHERE id = :id")
    fun updateAlarmText(id:Long,newText:String)

    @Delete
    fun delete(task : Task)

    @Query("DELETE FROM tasks_table")
    fun clear()

    @Query("SELECT * FROM tasks_table ORDER BY priority DESC")
    fun getAllTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE selected = 1")
    fun getSelectedTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    fun getTaskById(id: Long) : Task?

    @Query("SELECT * FROM tasks_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery ")
    fun searchDatabase(searchQuery : String) : LiveData<List<Task>>

    @Query("SELECT COUNT(*) FROM tasks_table WHERE selected=1")
    fun getAllSelectedTasks(): LiveData<Int>

    @Query("UPDATE tasks_table SET selected = 0 WHERE id = :id")
    fun unselectTask(id : Long)

    @Query("UPDATE tasks_table SET selected = 1 WHERE id = :id")
    fun selectTask(id : Long)

    @Query("UPDATE tasks_table SET checkState = 0 WHERE id = :id")
    fun uncheckTask(id : Long)

    @Query("UPDATE tasks_table SET checkState = 1 WHERE id = :id")
    fun checkTask(id : Long)

    @Query("UPDATE tasks_table SET selected = 1")
    fun selectAllTasks(): Int

    @Query("UPDATE tasks_table SET selected = 0")
    fun unselectAllTasks(): Int

    @Query("DELETE FROM tasks_table WHERE selected=1")
    fun deleteSelectedTasks(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM tasks_table WHERE id = :id)")
    fun taskExists(id: Long): Boolean

}