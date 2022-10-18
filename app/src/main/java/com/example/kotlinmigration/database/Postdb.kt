package com.example.kotlinmigration.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.kotlinmigration.database.dao.PostDao
import com.example.kotlinmigration.database.dto.PostDto
import com.example.kotlinmigration.models.API.PostsJsonItem

@Database(
    entities = [
        PostDto::class,
    ],
    version = 2
)

abstract class Postdb : RoomDatabase()
{
    abstract fun postDao(): PostDao

    companion object{

        const val DATABASE_NAME = "my-database"


    }
}