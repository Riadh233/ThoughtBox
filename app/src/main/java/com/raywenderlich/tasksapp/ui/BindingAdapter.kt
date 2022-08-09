package com.raywenderlich.tasksapp.ui

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("minValue")
fun setMin(picker : NumberPicker,min : Int){
    picker.minValue = min
}
@BindingAdapter("maxValue")
fun setMax(picker : NumberPicker,max : Int){
    picker.maxValue = max
}
