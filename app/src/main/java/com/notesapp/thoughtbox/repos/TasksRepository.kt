package com.notesapp.thoughtbox.repos
import androidx.lifecycle.LiveData
import com.notesapp.thoughtbox.R
import com.notesapp.thoughtbox.data.Task
import com.notesapp.thoughtbox.data.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksRepository(private val dao : TasksDao) {

    suspend fun delete(task: Task) {
        withContext(Dispatchers.IO) {
            dao.delete(task)
        }
    }

    suspend fun insertDataToDatabase(
        id:Long,
        title: String, description: String, taskPriority: Int, time: String,
        scheduledDate: String
    ) {
        withContext(Dispatchers.IO){
            dao.insert(Task(id,title, description,taskPriority,time,scheduledDate))
        }
    }
    fun searchDatabase(query : String) : LiveData<List<Task>>{
        return dao.searchDatabase(query)
    }

    fun getAllTasks() = dao.getAllTasks()
    fun getSelectedTasks() = dao.getSelectedTasks()
    fun getSelectedItemsCount() :LiveData<Int> = dao.getAllSelectedTasks()

    suspend fun getTaskById(id : Long) : Task?{
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
            }else
                dao.update(Task(task.id,task.title,task.description, R.color.light_gray,"Expired",task.scheduledDate,task.selected,true))
        }
    }
    suspend fun updateData(
        id:Long,
        title:String, description:String, taskPriority: Int, time: String,
        scheduledDate: String
    ){
        withContext(Dispatchers.IO){
            dao.update(Task(id,title,description,taskPriority,time, scheduledDate))
        }
    }
    suspend fun updateAlarmText(id : Long){
        withContext(Dispatchers.IO){
            dao.updateAlarmText(id,"Expired")
        }
    }

}