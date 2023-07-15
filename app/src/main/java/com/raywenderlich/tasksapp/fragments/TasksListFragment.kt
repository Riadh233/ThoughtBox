package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.data.Task
import com.raywenderlich.tasksapp.databinding.FragmentTasksBinding
import com.raywenderlich.tasksapp.ui.TasksAdapter
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel
import com.raywenderlich.tasksapp.viewmodels.TasksViewModel
import java.text.SimpleDateFormat
import java.util.*

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
    ): View {
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
        binding.recyclerView.setOnScrollChangeListener{_,scrollX,scrollY,_,oldScrollY ->
            when{
                scrollY > oldScrollY -> {
                    binding.FABText.isVisible = false
                }
                else -> {
                    binding.FABText.isVisible = true
                }
            }
        }
        binding.addTaskButton.setOnClickListener {
            unselectTasks()
            sharedViewModel.hideCAB()
            sharedViewModel.navigateToTasksScreen()
            findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragment2ToAddTaskFragment())
        }
        binding.FABLayout.setOnClickListener {
            unselectTasks()
            sharedViewModel.hideCAB()
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
        }, TasksAdapter.OnCheckChangeListener{
            viewModel.changeCheckState(it)
            if(!it.checkState){
                sharedViewModel.cancelAlarm(it)
            }
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
                if(it.isEmpty()){
                    binding.imgEmpty.visibility = View.VISIBLE
                    binding.textEmpty.visibility =View.VISIBLE
                }else{
                    binding.imgEmpty.visibility = View.GONE
                    binding.textEmpty.visibility =View.GONE
                }
            }
            val currentTime = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            for(task in it){
                if(alarmIsSet(task.alarmTime)){
                    val length = task.alarmTime.length
                    val taskTime = timeFormat.parse(task.alarmTime.substring(length-8,length))
                    val taskCalendar = Calendar.getInstance()
                    taskCalendar.time = taskTime
                    if(currentTime.get(Calendar.DAY_OF_YEAR) == taskCalendar.get(Calendar.DAY_OF_YEAR)
                        && taskCalendar.timeInMillis > currentTime.timeInMillis){
                        val time = "Rings Today $taskTime"
                        viewModel.updateData(task.id,task.title,task.description,task.priority,time)
                    }
                }
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

        sharedViewModel.onDeleteTasksEvent.observe(viewLifecycleOwner) { shouldConsumeEvent ->
            if (shouldConsumeEvent) {
                Snackbar.make(requireView(), "Delete Selected Tasks ?", Snackbar.LENGTH_LONG)
                    .setAction("Ok") {
                        deleteSelectedItems()
                        sharedViewModel.cancelAlarms(deletedTasks)
                        sharedViewModel.hideCAB()
                    }.show()
                sharedViewModel.consumeTasksDeletionEvent()
            }
        }

        sharedViewModel.onSelectAllTasks.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if (shouldConsumeEvent){
                selectAllItems()
                sharedViewModel.consumeSelectAllTasksEvent()
            }
        }

        sharedViewModel.onCancelTasksEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                unselectTasks()
                sharedViewModel.consumeCancelTasksEvent()
            }
        }
    }

    private fun alarmIsSet(alarmTime: String): Boolean {
        val alarm = alarmTime.toCharArray()
        return alarm[alarm.size-1] == 'm'
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