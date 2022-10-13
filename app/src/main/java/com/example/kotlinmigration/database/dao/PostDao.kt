package com.example.kotlinmigration.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlinmigration.database.dto.PostDto

@Dao
interface PostDao {

    @Insert
    suspend fun addPost(vararg postsDto: PostDto)

    @Query("SELECT * FROM posts_table")
    suspend fun readAllData(): List<PostDto>
}