package com.notesapp.thoughtbox.tools

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.AvailableNetworkInfo.PRIORITY_HIGH
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.notesapp.thoughtbox.MainActivity
import com.notesapp.thoughtbox.R
import com.notesapp.thoughtbox.data.NoteDatabase
import com.notesapp.thoughtbox.repos.TasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val tasksRep = context?.let { TasksRepository(NoteDatabase.getInstance(it.applicationContext).taskDao()) } ?: return
        intent  ?: return
        val taskId = intent.getLongExtra("taskId",-1)
        if(taskId != -1L){
            CoroutineScope(Dispatchers.IO).launch {
                 tasksRep.updateAlarmText(taskId)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val task = tasksRep.getTaskById(taskId)
            if (task != null) {
                val taskTitle = task.title
                showNotification(context, taskTitle)
            }
        }
    }

    private fun showNotification(context: Context?, taskTitle: String) {
        val i = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context!!, "channel_id")
            .setContentText("Tap to check out your task !")
            .setSmallIcon(R.drawable.splash_logo).setContentTitle(taskTitle)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(PRIORITY_HIGH).setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(12, builder.build())
    }
}