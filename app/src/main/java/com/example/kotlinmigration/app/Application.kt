package com.example.kotlinmigration.app

import android.app.Application
import android.content.Context

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: Application? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    /*Override fun onCreate() {
        super.onCreate()
    } */
}