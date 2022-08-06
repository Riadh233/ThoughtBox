package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.ui.NotesAdapter
import com.raywenderlich.tasksapp.databinding.FragmentListBinding

class ListFragment : Fragment(),SearchView.OnQueryTextListener {
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        adapter = NotesAdapter(NotesAdapter.NotesListener {
                viewModel.displayUpdateScreen(it)
        })
        binding.recyclerView.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            it?.let {
                this.findNavController().navigate(ListFragmentDirections.actionListFragmentToAddFragment())
            }
        }
        viewModel.navigateToAddFragment.observe(viewLifecycleOwner, Observer {
             if(it != null) {
                 findNavController().navigate(
                     ListFragmentDirections.actionListFragmentToUpdateFragment(it)
                 )
                 viewModel.navigateToUpdateScreenFinished()
             }

        })
        viewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })
        setHasOptionsMenu(true)

        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.items_menu,menu)

        val search = menu.findItem(R.id.search_item)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
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

        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_item)
            deleteAllUser()

        return super.onOptionsItemSelected(item)
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