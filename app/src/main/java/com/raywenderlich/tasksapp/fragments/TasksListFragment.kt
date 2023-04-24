package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.data.Task
import com.raywenderlich.tasksapp.databinding.FragmentTasksBinding
import com.raywenderlich.tasksapp.ui.TasksAdapter
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel
import com.raywenderlich.tasksapp.viewmodels.TasksViewModel

class TasksListFragment : Fragment(),SearchView.OnQueryTextListener {

    private val viewModel: TasksViewModel by lazy {
        ViewModelProvider(this)[TasksViewModel::class.java]
    }
    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }
    private lateinit var adapter: TasksAdapter
    private lateinit var binding: FragmentTasksBinding
    private lateinit var deletedTasks : List<Task>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpAddButton()
        setUpSearchView(binding.search)
        setupObservers()
    }

    private fun setUpAddButton() {
        binding.addTaskButton.setOnClickListener {
            sharedViewModel.navigateToTasksScreen()
            findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragment2ToAddTaskFragment())
        }
    }


    private fun setUpRecyclerView() {
        adapter = TasksAdapter(TasksAdapter.TasksClickListener{
            viewModel.displayUpdateScreen(it)
            sharedViewModel.navigateToTasksScreen()
        }, TasksAdapter.LongClickListener {
            sharedViewModel.showCAB()
        }, TasksAdapter.OnSelectItem{
            viewModel.selectItem(it)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = adapter
    }
    private fun setUpSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(this)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null)
            searchDatabase(query)
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null)
            searchDatabase(query)
        return true
    }
    private fun searchDatabase(query : String){
        val searchQuery = "%$query%"

        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }
    }
    private fun setupObservers() {

        viewModel.getAllTasks().observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }
        viewModel.getSelectedItemsCount().observe(viewLifecycleOwner){
            sharedViewModel.setSelectedItemsCount(it)
            if(it == 0){
                sharedViewModel.hideCAB()
            }
        }
        viewModel.navigateToAddFragment.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(
                    ViewPagerFragmentDirections.actionViewPagerFragment2ToAddTaskFragment(it)
                )
                viewModel.navigateToUpdateScreenFinished()
            }
        }
        viewModel.getSelectedTasks().observe(viewLifecycleOwner){
            deletedTasks = it
        }

        sharedViewModel.onDeleteTasksEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                deleteSelectedItems()
                sharedViewModel.cancelAlarms(deletedTasks)
                sharedViewModel.consumeTasksDeletionEvent()
            }
        }

        sharedViewModel.onCancelEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                unselectTasks()
                sharedViewModel.consumeCancelEvent()
            }
        }
        sharedViewModel.onSelectAllTasks.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if (shouldConsumeEvent){
                selectAllItems()
                sharedViewModel.consumeSelectAllTasksEvent()
            }
        }
    }
    private fun deleteSelectedItems() {
        viewModel.deleteSelectedTasks()
        adapter.notifyDataSetChanged()
    }

    private fun selectAllItems(){
        viewModel.selectAllTasks()
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        unselectTasks()
    }
    private fun unselectTasks() {
        viewModel.unselectTasks()
    }

}