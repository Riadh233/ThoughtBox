package com.raywenderlich.tasksapp.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raywenderlich.tasksapp.fragments.ListFragment
import com.raywenderlich.tasksapp.fragments.TasksFragment

class ViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentsList = listOf(
        ListFragment(), TasksFragment()
    )
    override fun getItemCount(): Int {
        return fragmentsList.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }
}