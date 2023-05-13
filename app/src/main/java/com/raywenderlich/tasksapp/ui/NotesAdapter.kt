package com.raywenderlich.tasksapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.tasksapp.data.Note
import com.raywenderlich.tasksapp.databinding.ListItemNoteBinding

class NotesAdapter(private val clickListener : ClickListener, private val longClickListener: LongClickListener, private val selectedItem: OnSelectItem) : ListAdapter<Note, NotesAdapter.ViewHolder>(DiffCallback) {
    private var isEnable = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnLongClickListener {
            longClickListener.onLongClick()
            selectedItem.onSelect(item)
            isEnable = true
            true
        }
        for (note in currentList){
            if(note.selected){
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
        holder.bind(item)
    }

    class ViewHolder(private var binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val selectIcon = binding.icSelected
        private val cardView = binding.view
        fun bind(item : Note){
            binding.title.text = item.title
            binding.description.text = item.description
            binding.number.text = item.date
            if (item.selected){
                selectItem()
            }else{
                unselectItem()
            }
            binding.executePendingBindings()
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
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class ClickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }

    class LongClickListener(val longClickListener: () -> Unit) {
        fun onLongClick() = longClickListener()
    }
    class OnSelectItem(val onItemSelect: (note: Note) -> Unit) {
        fun onSelect(note: Note) = onItemSelect(note)
    }
}
