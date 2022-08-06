package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        binding.button.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val priority  = binding.priority.value
        if(inputCheck(title,description)){
            viewModel.insertNote(Note(0,title,description,priority))
            Toast.makeText(requireContext(), "Task Added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToListFragment())
        }else{
            Toast.makeText(requireContext(), "please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
    }
    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }
}