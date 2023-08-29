package com.notesapp.thoughtbox.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.notesapp.thoughtbox.R
import com.notesapp.thoughtbox.tools.Priority

class SpinnerAdapter(context: Context, private val priorities: List<Priority>) :
    ArrayAdapter<Priority>(context, R.layout.custom_spinner_item, priorities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val priority = priorities[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = priority.label
        textView.setTextColor(ContextCompat.getColor(context, R.color.spinner_text_color))
        textView.setTypeface(null, Typeface.BOLD)
        textView.compoundDrawablePadding = 10 // set the margin between icon and text
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(priority.icon, 0, 0, 0)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item_priority, parent, false)
        val priority = priorities[position]
        val priorityLabel = view.findViewById<TextView>(R.id.tvPriorityLabel)
        val priorityIcon = view.findViewById<ImageView>(R.id.ivPriorityIcon)
        priorityLabel.text = priority.label
        priorityLabel.setTextColor(ContextCompat.getColor(context, R.color.spinner_text_color)) // Set dropdown text color
        priorityIcon.setImageResource(priority.icon)
        return view
    }
}
