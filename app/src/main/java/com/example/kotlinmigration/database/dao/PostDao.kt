package com.example.kotlinmigration.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlinmigration.database.dto.PostDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Insert
    suspend fun addPost(vararg postDto: PostDto)

    @Query("SELECT * FROM posts_table")
    suspend fun readAllData(): List<PostDto>

    @Query("DELETE FROM posts_table")
    suspend fun deleteAllData()
}