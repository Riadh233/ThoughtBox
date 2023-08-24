package com.raywenderlich.tasksapp.repos
import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.NoteDao
import com.raywenderlich.tasksapp.fragments.AddNoteFragmentArgs
import com.raywenderlich.tasksapp.tools.IDGenerator
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext


class NoteRepository(private val dao: NoteDao) {

    suspend fun delete(note: Note) {
        withContext(IO) {
            dao.delete(note)
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
    @SuppressLint("SuspiciousIndentation")
    suspend fun insertDataToDatabase(etTitle : EditText, etDescription :
                                     EditText, date : String, color : Int) {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

            withContext(IO){
            dao.insert(Note(IDGenerator.generateID(),title,description,date,false, color))
            }
    }
    @SuppressLint("SuspiciousIndentation")
    suspend fun updateData(
        etTitle: EditText, etDescription: EditText,
        date: String, args: AddNoteFragmentArgs, color: Int
    ){
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

            withContext(IO){
                dao.update(Note(args.currNote!!.id,title,description,date,false, color))
            }
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
        Log.d("delete from notes","done")
        withContext(IO){
            dao.deleteSelectedNotes()
        }
    }
}