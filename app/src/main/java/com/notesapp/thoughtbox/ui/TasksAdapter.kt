package com.notesapp.thoughtbox.ui
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
import com.notesapp.thoughtbox.R
import com.notesapp.thoughtbox.data.Task
import com.notesapp.thoughtbox.databinding.ListItemTaskBinding
import com.notesapp.thoughtbox.tools.HIGH_PRIORITY
import com.notesapp.thoughtbox.tools.LOW_PRIORITY
import com.notesapp.thoughtbox.tools.MID_PRIORITY

class TasksAdapter(private val clickListener : TasksClickListener, private val longClickListener: LongClickListener, private val selectedItem: OnSelectItem, private val checkListener : OnCheckChangeListener) : ListAdapter<Task, TasksAdapter.ViewHolder>(DiffCallback) {
    private var isEnable = false
    class ViewHolder(val binding : ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        private val selectIcon = binding.icSelected
        private val cardView = binding.view
        private val frameLayout = binding.frameLayout
        val title = binding.title
        fun bind(item : Task){
            binding.title.text = item.title
            binding.description.text = item.description
            binding.alarm.text = item.alarmTime
            binding.checkbox.isChecked = item.checkState
            frameLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, getColorForPriority(item.priority)))
            if (item.selected){
                selectItem()
            }else{
                unselectItem()
            }
            if(item.checkState){
                checkedMode()
            }else title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            binding.executePendingBindings()
            val unwrappedDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.GREEN)
        }

        private fun getColorForPriority(priority: Int): Int {
            return when(priority){
                LOW_PRIORITY -> R.color.light_blue
                MID_PRIORITY -> R.color.orange
                HIGH_PRIORITY -> R.color.red
                else -> R.color.light_gray
            }
        }

        private fun checkedMode() {
            title.setTextColor(ContextCompat.getColor(binding.root.context, R.color.spinner_text_color))
            title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        private fun selectItem() {
            selectIcon.isVisible = true
            cardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.item_selection_color))
            cardView.elevation = 0F
            frameLayout.elevation = 0F
        }
        private fun unselectItem(){
            cardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.card_default_color))
            frameLayout.elevation = 8F
            cardView.elevation = 5F
            selectIcon.isVisible = false
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
            else {
                selectedItem.onSelect(item)
            }
        }
        holder.bind(item)
        holder.binding.checkbox.setOnClickListener {
            checkListener.onCheckStateChanged(item)
        }
        Log.d("RECYCLER_VIEW_TAG","onBindViewHOlder called :${item.title}")
        Log.d("RECYCLER_VIEW_TAG","isEnabled :$isEnable")
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
    fun itemsDeletionFinished(){
        isEnable = false
    }
}