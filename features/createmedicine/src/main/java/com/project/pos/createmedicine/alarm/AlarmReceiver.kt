package com.project.pos.createmedicine.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.project.pos.createmedicine.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: return
        val id = intent.getIntExtra("EXTRA_ID", 0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, "medicine_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hora do remédio!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(id, builder.build())
//        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()
    }
}