package com.raywenderlich.tasksapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.ui.NotesAdapter
import com.raywenderlich.tasksapp.databinding.FragmentListBinding
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel

class NotesListFragment : Fragment(),SearchView.OnQueryTextListener {

    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }
    private lateinit var adapter: NotesAdapter
    private lateinit var binding : FragmentListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = binding.search
        setUpRecyclerView(binding)
        setUpAddButton(binding)
        setupObservers()
        setUpSearchView(searchView)
    }

    private fun unselectNotes() {
        viewModel.unselectNotes()
    }

    private fun setUpAddButton(binding: FragmentListBinding) {

        binding.addListButton.setOnClickListener {
            it?.let {
                unselectNotes()
                sharedViewModel.hideCAB()
                Toast.makeText(requireContext(), "frag paused", Toast.LENGTH_SHORT)
                findNavController()
                            .navigate(ViewPagerFragmentDirections.actionViewPagerFragment2ToAddFragment())

            }
        }
    }

    private fun setUpSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(this)
    }

    private fun setUpRecyclerView(binding: FragmentListBinding) {
        adapter = NotesAdapter(NotesAdapter.ClickListener {
            viewModel.displayUpdateScreen(it)
        }, NotesAdapter.LongClickListener {
            sharedViewModel.showCAB()
        }, NotesAdapter.OnSelectItem{
            viewModel.selectItem(it)
        })

        val manager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }
    private fun setupObservers() {

        viewModel.getAllNotes().observe(viewLifecycleOwner) {
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
                    ViewPagerFragmentDirections.actionViewPagerFragment2ToAddFragment(it)
                )
                viewModel.navigateToUpdateScreenFinished()
            }
        }
        sharedViewModel.onDeleteEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                deleteSelectedItems()
                sharedViewModel.consumeDeletionEvent()
            }
        }

        sharedViewModel.onCancelEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                unselectNotes()
                sharedViewModel.consumeCancelEvent()
            }
        }
        sharedViewModel.onSelectAll.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if (shouldConsumeEvent){
                selectAllItems()
                sharedViewModel.consumeSelectAllEvent()
            }
        }
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

        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })
    }
    private fun deleteSelectedItems() {
        viewModel.deleteSelectedNotes()
        adapter.notifyDataSetChanged()
    }

    private fun selectAllItems(){
        viewModel.selectAllNotes()
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        unselectNotes()
    }

}