package com.example.kotlinmigration.database.repository

import androidx.lifecycle.LiveData
import com.example.kotlinmigration.database.Postdb
import com.example.kotlinmigration.database.dao.PostDao
import com.example.kotlinmigration.models.API.PostsJsonItem

class PostRepository(private val postDao: PostDao) {

    val readAllData: LiveData<List<PostsJsonItem>> = postDao.readAllData()

    suspend fun addPost(postdb: Postdb) {

        postDao.addPost(postdb)
    }

}