package com.raywenderlich.tasksapp.fragments
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.ui.NotesAdapter
import com.raywenderlich.tasksapp.databinding.FragmentListNotesBinding
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel

class NotesListFragment : Fragment(),SearchView.OnQueryTextListener {

    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }
    private lateinit var adapter: NotesAdapter
    private lateinit var binding : FragmentListNotesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListNotesBinding.inflate(inflater)

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

    private fun setUpAddButton(binding: FragmentListNotesBinding) {
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

        binding.FABLayout.setOnClickListener {
            it?.let {
                fabButtonClicked()
            }
        }
        binding.addListButton.setOnClickListener {
            it?.let {
               fabButtonClicked()
            }
        }
    }

    private fun fabButtonClicked() {
        unselectNotes()
        sharedViewModel.hideCAB()
        sharedViewModel.navigateToNotesScreen()
        findNavController()
            .navigate(ViewPagerFragmentDirections.actionViewPagerFragment2ToAddFragment())
    }

    private fun setUpSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(this)
    }

    private fun setUpRecyclerView(binding: FragmentListNotesBinding) {
        adapter = NotesAdapter(NotesAdapter.ClickListener {
            viewModel.displayUpdateScreen(it)
            sharedViewModel.navigateToNotesScreen()
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
                if(it.isEmpty()){
                    binding.imgEmpty.apply {
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
        sharedViewModel.onDeleteNotesEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                Snackbar.make(requireView(), "Delete Selected Notes ?", Snackbar.LENGTH_LONG)
                    .setAction("Ok") {
                        deleteSelectedItems()
                        sharedViewModel.hideCAB()
                    }.show()
                sharedViewModel.consumeNotesDeletionEvent()
            }
        }
        sharedViewModel.onCancelNotesEvent.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if(shouldConsumeEvent){
                unselectNotes()
                sharedViewModel.consumeCancelNotesEvent()
            }
        }
        sharedViewModel.onSelectAllNotes.observe(viewLifecycleOwner){ shouldConsumeEvent ->
            if (shouldConsumeEvent){
                selectAllItems()
                sharedViewModel.consumeSelectAllNotesEvent()
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

        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) {
            it.let {
                if(query.isNotEmpty())
                    adapter.submitList(it)
                else
                    setupObservers()
            }
        }
    }
    private fun deleteSelectedItems() {
        viewModel.deleteSelectedNotes()
        adapter.itemsDeletionFinished()
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