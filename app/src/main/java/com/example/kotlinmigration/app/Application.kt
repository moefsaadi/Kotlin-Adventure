package com.example.kotlinmigration.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.kotlinmigration.database.Postdb

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: Application? = null

        lateinit var room: Postdb

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        room = Room.databaseBuilder(
            applicationContext,
            Postdb::class.java,
            Postdb.DATABASE_NAME
        ).build()

        super.onCreate()
    }
}