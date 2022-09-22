package com.example.kotlinmigration.API

import retrofit2.Call
import retrofit2.http.GET

interface ServiceAPI {

    @GET("posts")
    fun getPosts(): Call<PostsJsonItem>

}