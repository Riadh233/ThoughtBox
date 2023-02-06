package com.raywenderlich.tasksapp.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.FragmentViewPagerBinding
import com.raywenderlich.tasksapp.ui.ViewPagerAdapter
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding
    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        binding.deleteBtn.setOnClickListener {
            sharedViewModel.onDelete()
        }

        binding.cancelBtn.setOnClickListener{
            sharedViewModel.onCancel()
        }
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
        sharedViewModel.deleteAndCancelIconVisibility.observe(viewLifecycleOwner) {
            binding.deleteBtn.isVisible = it
            binding.cancelBtn.isVisible = it
        }
    }


}