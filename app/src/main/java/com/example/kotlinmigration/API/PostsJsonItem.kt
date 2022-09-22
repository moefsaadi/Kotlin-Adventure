package com.example.kotlinmigration.API

data class PostsJsonItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)