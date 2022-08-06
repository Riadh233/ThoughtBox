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
            viewModel.insertDataToDatabase(requireContext(),findNavController(), binding.etTitle,binding.etDescription,binding.priority)
        }

        return binding.root
    }
}