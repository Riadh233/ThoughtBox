package com.raywenderlich.tasksapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tasks_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val title : String,
    val description : String,
    val priority : Int,
    var alarmTime: String,
    val selected : Boolean = false,
    val checkState : Boolean = false
) : Parcelable