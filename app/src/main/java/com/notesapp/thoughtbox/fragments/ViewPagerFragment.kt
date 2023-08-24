package com.notesapp.thoughtbox.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.notesapp.thoughtbox.MainActivity
import com.notesapp.thoughtbox.R
import com.notesapp.thoughtbox.databinding.FragmentViewPagerBinding
import com.notesapp.thoughtbox.ui.ViewPagerAdapter
import com.notesapp.thoughtbox.viewmodels.SharedViewModel

class ViewPagerFragment : Fragment() {
    private var mActionMode: ActionMode? = null
    private var isActionModeActive = false
    private var onBackPressedCallback: OnBackPressedCallback? = null

    private lateinit var binding: FragmentViewPagerBinding
    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupObservers()

        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
               requireActivity().finish()
            }
        }
        onBackPressedCallback?.let {
            requireActivity().onBackPressedDispatcher.addCallback(this, it)
        }
    }


    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_notes)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_check)
                }
            }
        }.attach()
    }
    private fun setupObservers() {
        sharedViewModel.cabVisibility.observe(viewLifecycleOwner) { showCAB ->
            if (showCAB) {
                startActionMode()
            }
            else{
                finishActionMode()
            }
        }
        sharedViewModel.selectedItemsCount.observe(viewLifecycleOwner) {
            if (it == 1)
                mActionMode?.title = "$it item selected"
            else
                mActionMode?.title = "$it items selected"
        }
        sharedViewModel.navigateToTasksScreen.observe(viewLifecycleOwner){
            if(it){
                binding.viewPager.setCurrentItem(1,false)
            }else binding.viewPager.setCurrentItem(0,false)
        }

    }

    private val mActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.menuInflater
            inflater?.inflate(R.menu.delete_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {

                R.id.delete_item -> {
                    if(binding.viewPager.currentItem == 0){
                        sharedViewModel.onDeleteNotes()
                    } else
                        sharedViewModel.onDeleteTasks()
                    true
                }
                R.id.select_all -> {
                    if (binding.viewPager.currentItem == 0)
                        sharedViewModel.selectAllNotesEvent()
                    else sharedViewModel.selectAllTasksEvent()

                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {

            mActionMode = null
            if(binding.viewPager.currentItem == 0)
            sharedViewModel.onCancelNotes()
            else
                sharedViewModel.onCancelTasks()
        }
    }

    private fun startActionMode() {
        if (mActionMode != null) {
            return
        }
        mActionMode = (activity as MainActivity).startActionMode(mActionModeCallback)

        isActionModeActive = true
    }

    private fun finishActionMode() {
        mActionMode?.finish()
        isActionModeActive = false
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(mActionMode != null){
            outState.putBoolean("isActionModeActive",isActionModeActive)
        }
    }

}