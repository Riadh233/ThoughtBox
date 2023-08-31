package com.notesapp.thoughtbox

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.notesapp.thoughtbox.databinding.ActivityMainBinding
import com.notesapp.thoughtbox.viewmodels.NoteViewModel
import com.notesapp.thoughtbox.viewmodels.SharedViewModel

class MainActivity : AppCompatActivity() {

    val viewModel : SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private val notesViewModel : NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        createNotificationChannel()
        window!!.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window!!.navigationBarColor = ContextCompat.getColor(this, R.color.card_white)
        setContentView(binding.root)
    }

    @Deprecated("Deprecated in Java")
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