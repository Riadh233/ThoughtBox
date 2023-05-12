package com.raywenderlich.tasksapp.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.data.Task
import com.raywenderlich.tasksapp.databinding.ListTasksBinding
import java.util.*


class TasksAdapter(val clickListener : TasksClickListener, val longClickListener: LongClickListener, val selectedItem: OnSelectItem,val checkListener : OnCheckChangeListener) : ListAdapter<Task, TasksAdapter.ViewHolder>(DiffCallback) {
    private var isEnable = false
    class ViewHolder(val binding : ListTasksBinding) : RecyclerView.ViewHolder(binding.root) {
        val selectIcon = binding.icSelected
        val cardView = binding.view
        val title = binding.title
        fun bind(item : Task){
            binding.title.text = item.title
            binding.description.text = item.description
            binding.alarm.text = item.alarmTime
            binding.checkbox.isChecked = item.checkState
            binding.frameLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, item.priority))
            if (item.selected){
                selectItem()
            }else{
                unselectItem()
            }
            if(item.checkState){
                checkedMode()
            }
            binding.executePendingBindings()
            val unwrappedDrawable = AppCompatResources.getDrawable(binding.root.context, com.raywenderlich.tasksapp.R.drawable.rounded_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.GREEN)
        }
        private fun checkedMode() {
            title.setTextColor(ContextCompat.getColor(binding.root.context, R.color.light_gray))
            title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            cardView.elevation = 0F
        }

        private fun selectItem() {
            selectIcon.isVisible = true
            cardView.setCardBackgroundColor(Color.parseColor("#d3d3d3"))
            cardView.elevation = 0F
        }
        private fun unselectItem(){
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"))
            cardView.elevation = 10F
            selectIcon.isVisible = false
            Log.d("unselectItem","true")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListTasksBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnLongClickListener {
            longClickListener.onLongClick()
            selectedItem.onSelect(item)
            isEnable = true
            true
        }
        for (task in currentList){
            if(task.selected){
                isEnable = true
                break
            }else
                isEnable = false
        }

        holder.itemView.setOnClickListener {
            if(!isEnable)
                clickListener.onClick(item)
            else
                selectedItem.onSelect(item)
        }
        Log.d("selectionItemState","${item.selected}")
        holder.bind(item)
        holder.binding.checkbox.setOnClickListener {
            checkListener.onCheckStateChanged(item)
        }
    }

    class TasksClickListener(val clickListener: (task: Task) -> Unit) {
        fun onClick(task: Task) = clickListener(task)
    }
    class LongClickListener(val longClickListener: () -> Unit) {
        fun onLongClick() = longClickListener()
    }
    class OnSelectItem(val onItemSelect: (task: Task) -> Unit) {
        fun onSelect(task: Task) = onItemSelect(task)
    }
    class OnCheckChangeListener(val checkListener: (task : Task) -> Unit) {
        fun onCheckStateChanged(task : Task) = checkListener(task)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
    }
}