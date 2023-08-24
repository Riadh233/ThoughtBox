package com.notesapp.thoughtbox.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.notesapp.thoughtbox.fragments.NotesListFragment
import com.notesapp.thoughtbox.fragments.TasksListFragment

class ViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentsList = listOf(
        NotesListFragment(), TasksListFragment()
    )
    override fun getItemCount(): Int {
        return fragmentsList.size
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NotesListFragment()
            else -> TasksListFragment()
        }
    }
}