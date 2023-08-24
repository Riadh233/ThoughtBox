package com.notesapp.thoughtbox.tools

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_NO_CREATE
import android.content.Context
import android.content.Intent
import com.notesapp.thoughtbox.data.Task

class AlarmUtils {
    private lateinit var alarmManager: AlarmManager
    private var alarmPendingIntents = mutableMapOf<Long, PendingIntent>()

    fun setAlarm(taskId : Long, timeScheduled:Long, application: Application){
        alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java)
        intent.putExtra("taskId",taskId)
        val pendingIntent = PendingIntent.getBroadcast(
            application,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeScheduled, pendingIntent)
        alarmPendingIntents[taskId] = pendingIntent
    }
    fun cancelAlarms(tasks: List<Task>, application: Context) {
        for(task in tasks){
            alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(application, AlarmReceiver::class.java)
            val pendingIntent : PendingIntent? = PendingIntent.getBroadcast(
                application,
                task.id.toInt(),
                intent,
                FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if(pendingIntent != null){
                alarmManager.cancel(pendingIntent)
            }
        }
    }
    fun cancelAlarm(task: Task, application: Context) {
        alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(application, AlarmReceiver::class.java)
        val pendingIntent: PendingIntent? =
            PendingIntent.getBroadcast(
                application,
                task.id.toInt(),
                intent,
                FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }
}