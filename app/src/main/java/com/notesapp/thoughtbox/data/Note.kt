package com.notesapp.thoughtbox.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.notesapp.thoughtbox.R
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val date: String,
    var selected: Boolean = false,
    val color : Int = -1

) : Parcelable
