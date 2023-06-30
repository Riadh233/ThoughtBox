package com.raywenderlich.tasksapp.repos
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.material.textfield.TextInputEditText
import com.raywenderlich.tasksapp.R
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
    suspend fun insertDataToDatabase(id:Long,title: String, description: String, taskPriority: Int, time: String) {
        withContext(Dispatchers.IO){
            dao.insert(Task(id,title, description,taskPriority,time))
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
    suspend fun changeCheckState(task : Task){
        withContext(Dispatchers.IO){
            if(task.checkState){
                dao.uncheckTask(task.id)
                Log.d("checkbox","uncheck method used")
            }else
                dao.update(Task(task.id,task.title,task.description, R.color.light_gray,"Expired",task.selected,true))
            Log.d("checkbox","check method used")
        }
    }
    suspend fun updateData(id:Long,title:String, description:String, taskPriority: Int, time: String){
        withContext(Dispatchers.IO){
            dao.update(Task(id,title,description,taskPriority,time))
        }
    }
    suspend fun updateAlarmText(id : Long){
        withContext(Dispatchers.IO){
            dao.updateAlarmText(id,"Expired")
        }
        Log.d("update text","done")
    }

}