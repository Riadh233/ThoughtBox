package com.raywenderlich.tasksapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.databinding.FragmentAddBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        binding.backButton.setOnClickListener {
                if ((inputCheck(binding.etTitle.text.toString(),binding.etDescription.text.toString())))
                    createNote()
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToViewPagerFragment2())
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if ((inputCheck(binding.etTitle.text.toString(),binding.etDescription.text.toString())))
                createNote()
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToViewPagerFragment2())
        }
        callback.isEnabled = true
        Log.d("CALLBACK","${callback.isEnabled}")


        return binding.root
    }
    private fun createNote(){
        viewModel.insertDataToDatabase(requireContext(),findNavController(), binding.etTitle,binding.etDescription,
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()))
    }

    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }
}