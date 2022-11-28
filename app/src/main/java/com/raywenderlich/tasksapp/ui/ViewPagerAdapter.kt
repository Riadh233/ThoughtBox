package com.raywenderlich.tasksapp.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raywenderlich.tasksapp.fragments.NotesListFragment
import com.raywenderlich.tasksapp.fragments.TasksListFragment

class ViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentsList = listOf(
        NotesListFragment(), TasksListFragment()
    )
    override fun getItemCount(): Int {
        return fragmentsList.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }
}