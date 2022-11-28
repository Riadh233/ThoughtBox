package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.FragmentTasksBinding
import com.raywenderlich.tasksapp.ui.NotesAdapter
import com.raywenderlich.tasksapp.ui.TasksAdapter
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.viewmodels.TasksViewModel

class TasksListFragment : Fragment() {
    private lateinit var viewModel: TasksViewModel
    private lateinit var adapter: TasksAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTasksBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        adapter = TasksAdapter(TasksAdapter.TasksClickListener {
            viewModel.displayUpdateScreen(it)
        })
        binding.recyclerView.adapter = adapter
        binding.addTaskButton.setOnClickListener {
            it?.let {
                this.findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragment2ToAddTaskFragment())
            }
        }
        viewModel.navigateToAddFragment.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                findNavController().navigate(
                    ViewPagerFragmentDirections.actionViewPagerFragment2ToUpdateTaskFragment(it)
                )
                viewModel.navigateToUpdateScreenFinished()
            }

        })


        return binding.root
    }
}