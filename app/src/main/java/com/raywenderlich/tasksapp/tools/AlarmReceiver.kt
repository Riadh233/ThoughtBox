package com.raywenderlich.tasksapp.tools

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.AvailableNetworkInfo.PRIORITY_HIGH
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.data.NoteDatabase
import com.raywenderlich.tasksapp.fragments.ViewPagerFragment
import com.raywenderlich.tasksapp.repos.TasksRepository
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel
import com.raywenderlich.tasksapp.viewmodels.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var alarmManager: AlarmManager
    override fun onReceive(context: Context?, intent: Intent?) {
        val tasksRep = context?.let { TasksRepository(NoteDatabase.getInstance(it.applicationContext).taskDao()) } ?: return
        intent  ?: return
        val taskId = intent.getLongExtra("taskId",-1)
        if(taskId != -1L){
            CoroutineScope(Dispatchers.IO).launch {
                 tasksRep.updateAlarmText(taskId)
            }
        }
        val i =  Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
        val builder =  NotificationCompat.Builder(context,"channel_id")
            .setContentText("Check your daily tasks")
            .setSmallIcon(R.drawable.ic_alarm) .setContentTitle("App name")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(PRIORITY_HIGH).setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(12,builder.build())
    }
}