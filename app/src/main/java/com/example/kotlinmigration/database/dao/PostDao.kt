package com.example.kotlinmigration.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlinmigration.database.Postdb
import com.example.kotlinmigration.models.API.PostsJsonItem

@Dao
interface PostDao {


    @Insert
    suspend fun addPost(postdb: Postdb)

    @Query("SELECT * FROM posts_table")
    fun readAllData(): LiveData<List<PostsJsonItem>> //Probably make this a flow instead
}