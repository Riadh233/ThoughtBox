package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.FragmentViewPagerBinding
import com.raywenderlich.tasksapp.ui.ViewPagerAdapter
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel

class ViewPagerFragment : Fragment() {
    private var mActionMode: ActionMode? = null

    private lateinit var binding: FragmentViewPagerBinding
    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupObservers()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_notes)
                1 -> tab.setIcon(R.drawable.ic_check)
            }
        }.attach()
    }

    private fun setupObservers() {
        sharedViewModel.cabVisibility.observe(viewLifecycleOwner) { showCAB ->
            if (showCAB)
                startActionMode()
            else
                finishActionMode()
        }
        sharedViewModel.selectedItemsCount.observe(viewLifecycleOwner) {
            if (it == 1)
                mActionMode?.title = "$it item selected"
            else
                mActionMode?.title = "$it items selected"
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
                    // delete selected items
                    sharedViewModel.onDelete()
                    mode?.finish() // Action picked, so close the CAB
                    true
                }
                R.id.select_all -> {
                    sharedViewModel.selectAllEvent()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            mActionMode = null
            sharedViewModel.onCancel()
        }
    }

    private fun startActionMode() {
        if (mActionMode != null) {
            return
        }
        mActionMode = (activity as MainActivity).startActionMode(mActionModeCallback)
    }

    private fun finishActionMode() {
        mActionMode?.finish()
    }

}