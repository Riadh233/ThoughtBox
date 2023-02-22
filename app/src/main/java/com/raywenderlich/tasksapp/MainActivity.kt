package com.raywenderlich.tasksapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.tasksapp.databinding.ActivityMainBinding
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel

class MainActivity : AppCompatActivity() {

    val viewModel : SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    val notesViewModel : NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if(viewModel.cabVisibility.value!!) {
            notesViewModel.unselectNotes()
        }else
            super.onBackPressed()
    }

}