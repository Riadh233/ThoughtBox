package com.notesapp.thoughtbox.ui

import android.R.bool
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.notesapp.thoughtbox.R
import com.notesapp.thoughtbox.data.Note
import com.notesapp.thoughtbox.databinding.ListItemNoteBinding


class NotesAdapter(private val clickListener : ClickListener, private val longClickListener: LongClickListener, private val selectedItem: OnSelectItem,private val context: Context) : ListAdapter<Note, NotesAdapter.ViewHolder>(DiffCallback) {
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

        holder.bind(item, context)

    }

    class ViewHolder(private var binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val selectIcon = binding.icSelected
        private val cardView = binding.view
        private var color :Int = -1
        fun bind(item : Note, context: Context){
            color = item.color
            binding.title.text = item.title
            binding.description.text = item.description
            binding.number.text = item.date
            binding.background.setBackgroundColor(item.color)

            if (item.selected){
                selectItem(context)
            }else{
                unselectItem()
            }
            binding.executePendingBindings()
        }
        @SuppressLint("ResourceAsColor")
        private fun selectItem(context: Context) {
            selectIcon.isVisible = true

            if(color == 16777215) {
                if(context.resources.getString(R.string.mode) == "dark")
                binding.background.setBackgroundColor(Color.GRAY)
                else binding.background.setBackgroundColor(Color.LTGRAY)
            }
            else {
                if(context.resources.getString(R.string.mode) == "dark"){
                    binding.background.setBackgroundColor(manipulateColor(color, 1.8f))}
                else
                    binding.background.setBackgroundColor(manipulateColor(color, 0.8f))
            }
            cardView.elevation = 0F
        }
        private fun unselectItem(){
            binding.background.setBackgroundColor(color)
            cardView.elevation = 10F
            selectIcon.isVisible = false
        }

        private fun manipulateColor(color: Int, factor: Float): Int {
            val a = Color.alpha(color)
            val r = Math.round(Color.red(color) * factor)
            val g = Math.round(Color.green(color) * factor)
            val b = Math.round(Color.blue(color) * factor)
            return Color.argb(
                a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255)
            )
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
    fun itemsDeletionFinished(){
        isEnable = false
    }
}
