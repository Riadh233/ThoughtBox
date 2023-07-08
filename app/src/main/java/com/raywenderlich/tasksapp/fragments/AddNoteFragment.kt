package com.raywenderlich.tasksapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.databinding.FragmentAddNoteBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: NoteViewModel
    private val args by navArgs<AddNoteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding.etTitle.setText(args.currNote?.title)
        binding.etDescription.setText(args.currNote?.description)

        binding.backButton.setOnClickListener {
            if (inputCheck(
                    binding.etTitle.text.toString(),
                    binding.etDescription.text.toString()
                )
            ) {
                if (args.currNote == null) {
                    createNote()
                } else
                    updateNote()
            }
            findNavController().navigate(AddNoteFragmentDirections.actionAddFragmentToViewPagerFragment2())
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isEnabled){
                    if((inputCheck(binding.etTitle.text.toString(),binding.etDescription.text.toString()))){
                        if(args.currNote == null)
                        createNote()
                        else updateNote()
                    }
                    isEnabled = false
                    findNavController().popBackStack()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etDesc = binding.etDescription
        etDesc.requestFocus()
        showKeyboard(etDesc)
    }
    private fun createNote(){
        viewModel.insertDataToDatabase(binding.etTitle,binding.etDescription,
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()))
    }
    private fun updateNote(){
        viewModel.updateData(binding.etTitle,binding.etDescription,
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()),args)
    }

    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }
    private fun showKeyboard(et : EditText) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

}