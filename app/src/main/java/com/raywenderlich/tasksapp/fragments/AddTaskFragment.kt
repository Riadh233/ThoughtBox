package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.FragmentAddBinding
import com.raywenderlich.tasksapp.databinding.FragmentAddTaskBinding
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel

class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        return binding.root
    }
}