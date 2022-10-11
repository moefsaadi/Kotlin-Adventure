package com.example.kotlinmigration.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.kotlinmigration.database.dao.PostDao
import com.example.kotlinmigration.models.API.PostsJsonItem

@Database(
    entities = [
        PostsJsonItem::class,
    ],
    version = 1
)

abstract class Postdb : RoomDatabase()
{
    abstract fun postDao(): PostDao

    companion object{

        const val DATABASE_NAME = "my-database"

        @Volatile
        private var INSTANCE: Postdb? = null

        fun getDatabase(context: Context): Postdb{
            val tempInstance = INSTANCE
            if(tempInstance !=null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Postdb::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}