package com.project.pos.tomaai.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.project.pos.createmedicine.alarm.AlarmItem
import com.project.pos.createmedicine.alarm.AlarmScheduler
import java.util.Calendar

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
            putExtra("EXTRA_ID", item.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, item.time.hour)
            set(Calendar.MINUTE, item.time.minute)
            set(Calendar.SECOND, 0)
        }
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

//        val triggerAtMillis = System.currentTimeMillis() + 10000
//        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun cancel(id: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}