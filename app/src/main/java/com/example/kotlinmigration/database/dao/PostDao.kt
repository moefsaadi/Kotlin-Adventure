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
    fun readAllData(): Flow<List<PostDto>> //Removed "suspend" not sure if i should

    @Query("DELETE FROM posts_table")
    suspend fun deleteAllData()
}