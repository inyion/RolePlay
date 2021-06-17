package com.samsia.roleplay

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App: Application() {

    companion object {
        lateinit var prefs :SharedPreferences
        lateinit var instance :Application
    }

    override fun onCreate() {
        prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        super.onCreate()
    }
}