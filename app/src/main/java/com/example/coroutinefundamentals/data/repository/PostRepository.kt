package com.example.coroutinefundamentals.data.repository

import com.example.coroutinefundamentals.data.model.Post
import com.example.coroutinefundamentals.utils.ErrorHandling
import kotlinx.coroutines.flow.Flow

interface PostRepository {
     fun getPosts(): Flow<ErrorHandling<List<Post>>>
}



