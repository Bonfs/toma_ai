package com.project.pos.tomaai

import android.app.Application
import com.google.firebase.FirebaseApp

class TomaAiApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}