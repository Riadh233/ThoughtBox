package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.databinding.FragmentUpdateBinding
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateFragment : Fragment() {
    private lateinit var binding : FragmentUpdateBinding
    private lateinit var viewModel : NoteViewModel
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("args","${args.currTask}")
        binding = FragmentUpdateBinding.inflate(inflater)
        binding.etTitle.setText(args.currTask.title)
        binding.etDescription.setText(args.currTask.description)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding.backButton.setOnClickListener {
            if ((inputCheck(binding.etTitle.text.toString(),binding.etDescription.text.toString())))
                updateNote()
            findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToViewPagerFragment2())
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if ((inputCheck(binding.etTitle.text.toString(),binding.etDescription.text.toString())))
                updateNote()
            findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToViewPagerFragment2())
        }
        callback.isEnabled = true

        return binding.root
    }
    private fun updateNote(){
        viewModel.updateData(requireContext(), findNavController(),
            binding.etTitle,binding.etDescription,
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()),args)
    }
    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description) )
    }
}