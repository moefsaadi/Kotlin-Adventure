package com.example.kotlinmigration.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.kotlinmigration.database.Postdb
import com.example.kotlinmigration.viewmodels.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: Application? = null

        lateinit var room: Postdb
        lateinit var retrofit: Retrofit

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

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        

        super.onCreate()
    }
}