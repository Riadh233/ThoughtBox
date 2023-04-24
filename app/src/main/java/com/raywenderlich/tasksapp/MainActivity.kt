package com.raywenderlich.tasksapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.tasksapp.databinding.ActivityMainBinding
import com.raywenderlich.tasksapp.fragments.AddTaskFragment
import com.raywenderlich.tasksapp.viewmodels.NoteViewModel
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel
import javax.xml.parsers.DocumentBuilderFactory.newInstance

class MainActivity : AppCompatActivity() {

    val viewModel : SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    val notesViewModel : NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        createNotificationChannel()
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if(viewModel.cabVisibility.value!!) {
            notesViewModel.unselectNotes()
        }else
            super.onBackPressed()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Tasks Notifications"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}