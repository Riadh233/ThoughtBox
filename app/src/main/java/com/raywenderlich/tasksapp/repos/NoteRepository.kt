package com.raywenderlich.tasksapp.repos
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.NoteDao
import com.raywenderlich.tasksapp.fragments.AddFragmentArgs
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
    suspend fun insertDataToDatabase(etTitle : EditText, etDescription :
                                     EditText, date : String) {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
            withContext(IO){
            dao.insert(Note(0,title,description,date))
            }
    }
    suspend fun updateData(
        etTitle: EditText, etDescription: EditText,
        date: String, args: AddFragmentArgs
    ){
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

            withContext(IO){
                dao.update(Note(args.currNote!!.id,title,description,date))
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
        withContext(IO){
            dao.deleteSelectedNotes()
        }
        Log.d("delete selected notes ","called")
    }
}