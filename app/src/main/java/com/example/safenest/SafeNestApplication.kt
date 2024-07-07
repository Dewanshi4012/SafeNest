package com.example.safenest

import android.app.Application

class SafeNestApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }
}