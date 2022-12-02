package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
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
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    private var mainMenu : Menu? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        adapter = NotesAdapter(NotesAdapter.ClickListener {
                viewModel.displayUpdateScreen(it)
        }, NotesAdapter.LongClickListener{
            sharedViewModel.showDeleteIcon()
        })

        val manager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter


        binding.addListButton.setOnClickListener {
            it?.let {
                this.findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragment2ToAddFragment())
            }
        }

        setupObservers()

        val searchView = binding.search
        searchView.setOnQueryTextListener(this)

        return binding.root
    }

    private fun setupObservers() {
        viewModel.navigateToAddFragment.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(
                    ViewPagerFragmentDirections.actionViewPagerFragment2ToUpdateFragment(it)
                )
                viewModel.navigateToUpdateScreenFinished()
            }

        })
        viewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        sharedViewModel.onDeleteEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                deleteSelectedItems()
                sharedViewModel.consumeDeletionEvent()
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
    private fun showDeleteMenu(show : Boolean){
    }

    private fun deleteSelectedItems() {
        Toast.makeText(requireContext(), "Delete clicked", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAllUser() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            viewModel.deleteAllNotes()
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_-> }
        builder.setTitle("Delete All Tasks ?")
        builder.setMessage("are you sure you want to delete All Tasks ?")
        builder.create().show()
    }


}