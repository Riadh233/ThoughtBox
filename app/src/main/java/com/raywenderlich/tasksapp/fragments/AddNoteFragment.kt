package com.raywenderlich.tasksapp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.BottomSheetLayoutBinding
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.databinding.FragmentAddNoteBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: NoteViewModel
    private var color = -1
    private val args by navArgs<AddNoteFragmentArgs>()
    private var onBackPressedCallback: OnBackPressedCallback? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        handleCurrVisitedNote()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etDesc = binding.etDescription
        etDesc.requestFocus()
        showKeyboard(etDesc)
        changeInsetsColor(color, false)
        handleColorPicker()
        handleBackButton()
    }


    private fun handleColorPicker() {
        binding.colorPicker.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )
            val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            with(bottomSheetDialog) {
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setOnColorSelectedListener { value ->
                        color = value
                        binding.apply {
                            coloredView.setBackgroundColor(color)
                            changeInsetsColor(color, false)
                        }
                    }
                }
            }
            bottomSheetView.post {
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

        }
    }

    private fun handleCurrVisitedNote() {
        binding.etTitle.setText(args.currNote?.title)
        binding.etDescription.setText(args.currNote?.description)
        binding.coloredView.setBackgroundColor(resources.getColor(R.color.card_white))
        args.currNote?.let {
            binding.coloredView.setBackgroundColor(it.color)
            color = args.currNote!!.color
        }
        if (color == -1)
            binding.coloredView.setBackgroundColor(resources.getColor(R.color.card_white))
    }

    private fun handleBackButton() {
        binding.backButton.setOnClickListener {
            changeInsetsColor(resources.getColor(R.color.blue), true)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    if ((inputCheck(
                            binding.etTitle.text.toString(),
                            binding.etDescription.text.toString()
                        ))
                    ) {
                        if (args.currNote == null) {
                            createNote()
                        } else {
                            updateNote()
                        }
                    }
                    changeInsetsColor(resources.getColor(R.color.blue), true)
                    isEnabled = false
                    findNavController().navigate(AddNoteFragmentDirections.actionAddFragmentToViewPagerFragment2())
                }
            }
        }
        onBackPressedCallback?.let {
            requireActivity().onBackPressedDispatcher.addCallback(this, it)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNote(){
        viewModel.insertDataToDatabase(binding.etTitle,binding.etDescription,
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()), color)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateNote(){
        viewModel.updateData(binding.etTitle,binding.etDescription,
            DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()),args, color)
    }

    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }
    private fun showKeyboard(et : EditText) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun changeInsetsColor(color: Int, backPressed : Boolean){
        if(backPressed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity?.window!!.statusBarColor = resources.getColor(R.color.blue)
                activity?.window!!.navigationBarColor = resources.getColor(R.color.white)
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity?.window!!.statusBarColor = color
                activity?.window!!.navigationBarColor = color
            }
        }
    }
    override fun onResume() {
        super.onResume()
        onBackPressedCallback?.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback?.isEnabled = false
    }
}