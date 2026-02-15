package com.example.coroutinefundamentals.data.remote.network

import com.example.coroutinefundamentals.data.model.Post
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
   suspend fun getPosts(): List<Post>
}

