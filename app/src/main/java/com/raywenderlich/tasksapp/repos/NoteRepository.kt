package com.raywenderlich.tasksapp.repos

import android.content.Context
import android.text.TextUtils
import android.util.Log
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
    suspend fun selectionItemState(note: Note){
        withContext(IO){
            if(note.selected)
                dao.unselectNote(note.id)
            else
                dao.selectNote(note.id)
        }
    }
    fun searchDatabase(query : String) : LiveData<List<Note>>{
        return dao.searchDatabase(query)
    }
    suspend fun insertDataToDatabase(context : Context,
                                     etTitle : EditText, etDescription :
                                     EditText, date : String) {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
            withContext(IO){
            dao.insert(Note(0,title,description,date))
            }
            Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
    }
    suspend fun updateData(
        context: Context,
        etTitle: EditText, etDescription: EditText,
        date: String, args: UpdateFragmentArgs
    ){
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

            withContext(IO){
                dao.update(Note(args.currTask.id,title,description,date))
            }
        Toast.makeText(context, "Note updated", Toast.LENGTH_SHORT).show()
    }

    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }

    fun getAllNotes() = dao.getAllNotes()
    fun getSelectedItemsCount() :LiveData<Int> = dao.getAllSelectedNotes()
    suspend fun unselectNotes() {
        withContext(IO){
            dao.unselectAllNotes()
        }
    }
    suspend fun selectAllNotes(){
        withContext(IO){
            dao.selectAllNotes()
        }
    }

    suspend fun deleteSelectedNotes() {
        withContext(IO){
            dao.deleteSelectedNotes()
        }
        Log.d("delete selected notes ","called")
    }
}