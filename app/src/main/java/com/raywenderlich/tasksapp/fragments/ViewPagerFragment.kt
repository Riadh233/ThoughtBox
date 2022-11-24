package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.FragmentViewPagerBinding
import com.raywenderlich.tasksapp.ui.ViewPagerAdapter

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentViewPagerBinding.inflate(layoutInflater)
        binding.actionBar.inflateMenu(R.menu.delete_menu)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater,container,false)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        binding.actionBar.inflateMenu(R.menu.delete_menu)

        viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.setIcon(R.drawable.ic_notes)
                1 -> tab.setIcon(R.drawable.ic_check)
            }
        }.attach()


        return binding.root
    }
}