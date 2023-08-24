package com.notesapp.thoughtbox.fragments

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
import com.notesapp.thoughtbox.MainActivity
import com.notesapp.thoughtbox.data.Task
import com.notesapp.thoughtbox.databinding.FragmentTasksBinding
import com.notesapp.thoughtbox.ui.TasksAdapter
import com.notesapp.thoughtbox.viewmodels.SharedViewModel
import com.notesapp.thoughtbox.viewmodels.TasksViewModel
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
        binding.recyclerView.setOnScrollChangeListener{_,_,scrollY,_,oldScrollY ->
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
                if(query.isNotEmpty())
                    adapter.submitList(it)
                else
                    setupObservers()
            }
        }
    }
    private fun setupObservers() {

        viewModel.getAllTasks().observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                if(it.isEmpty()){
                    binding.imgEmpty.apply {
                        visibility = View.VISIBLE
                        visibility = View.VISIBLE
                        alpha = 0f // Initially set the alpha to 0
                        animate().alpha(1f).setDuration(1000).start()
                    }
                    binding.textEmpty.apply {
                        visibility = View.VISIBLE
                        alpha = 0f // Initially set the alpha to 0
                        animate().alpha(1f).setDuration(1000).start()
                    }
                }else{
                    binding.imgEmpty.visibility = View.GONE
                    binding.textEmpty.visibility =View.GONE
                }
                checkAlarmTimeState(it)

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

    private fun checkAlarmTimeState(it: List<Task>) {
        for (task in it) {
            if (isSameDay(task.scheduledDate) && task.alarmTime.length > 15 && task.alarmTime.substring(0, 15)
                    .trim() == "Rings Tomorrow"
            ) {
                val alarmTime = "Rings Today ${task.alarmTime.substring(15)}"
                viewModel.updateData(
                    task.id,
                    task.title,
                    task.description,
                    task.priority,
                    alarmTime,
                    task.scheduledDate
                )
                adapter.notifyDataSetChanged()
            }
        }
    }
    private fun deleteSelectedItems() {
        viewModel.deleteSelectedTasks()
        adapter.itemsDeletionFinished()
    }

    private fun selectAllItems(){
        viewModel.selectAllTasks()
    }

    override fun onPause() {
        super.onPause()
        unselectTasks()
    }
    private fun unselectTasks() {
        viewModel.unselectTasks()
    }

    private fun isSameDay(savedTimeString: String): Boolean {
        val savedFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.getDefault())
        val currentFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val savedDate = savedFormat.parse(savedTimeString)
        val currentDate = Date()

        val savedDateString = currentFormat.format(savedDate)
        val currentDateString = currentFormat.format(currentDate)

        return savedDateString == currentDateString
    }
}