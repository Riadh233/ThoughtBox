package com.raywenderlich.tasksapp.ui

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter

@BindingAdapter("minValue")
fun setMin(picker : NumberPicker,min : Int){
    picker.minValue = min
}
@BindingAdapter("maxValue")
fun setMax(picker : NumberPicker,max : Int){
    picker.maxValue = max
}
