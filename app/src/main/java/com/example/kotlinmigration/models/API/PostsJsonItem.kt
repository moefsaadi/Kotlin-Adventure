package com.example.kotlinmigration.models.API

import com.example.kotlinmigration.database.dto.PostDto

data class PostsJsonItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int

)