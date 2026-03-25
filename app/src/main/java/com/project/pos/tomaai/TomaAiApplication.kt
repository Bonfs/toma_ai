package com.project.pos.tomaai

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.puc.di.tomaAiModules
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TomaAiApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        createNotificationChannel()
        startKoin {
            androidLogger()
            androidContext(this@TomaAiApplication)
            modules(tomaAiModules)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "medicine_channel",
            "Medicine",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Channel for medicine reminders"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}