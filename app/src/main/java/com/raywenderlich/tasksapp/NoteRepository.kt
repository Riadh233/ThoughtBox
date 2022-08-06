package com.raywenderlich.tasksapp

import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.NoteDao
import com.raywenderlich.tasksapp.fragments.AddFragmentDirections
import com.raywenderlich.tasksapp.fragments.UpdateFragmentArgs
import com.raywenderlich.tasksapp.fragments.UpdateFragmentDirections
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext


class NoteRepository(private val dao: NoteDao) {

    suspend fun insert(note: Note) {
        withContext(IO) {
            dao.insert(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(IO) {
            dao.update(note)
        }
    }

    suspend fun delete(note: Note) {
        withContext(IO) {
            dao.delete(note)
        }
    }


    suspend fun deleteAllNotes() {
        withContext(IO) {
            dao.clear()
        }
    }
    fun searchDatabase(query : String) : LiveData<List<Note>>{
        return dao.searchDatabase(query)
    }
    suspend fun insertDataToDatabase(context : Context, navController : NavController,
                                     etTitle : EditText, etDescription :
                                     EditText, picker : NumberPicker) {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority  = picker.value
        if(inputCheck(title,description)){
            withContext(IO){
            dao.insert(Note(0,title,description,priority))
            }
            Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
            navController.navigate(AddFragmentDirections.actionAddFragmentToListFragment())
        }else{
            Toast.makeText(context, "please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
    }
    suspend fun updateData(context : Context, navController : NavController,
                           etTitle : EditText, etDescription : EditText,
                           picker : NumberPicker , args : UpdateFragmentArgs ){
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority = picker.value
        if(inputCheck(title,description)){
            withContext(IO){
                dao.update(Note(args.currTask.id,title,description,priority))
            }
            navController.navigate(UpdateFragmentDirections.actionUpdateFragmentToListFragment())
        }else
            Toast.makeText(context, "please fill out all fields", Toast.LENGTH_SHORT).show()
    }

    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }

    fun getAllNotes() = dao.getAllNotes()
}