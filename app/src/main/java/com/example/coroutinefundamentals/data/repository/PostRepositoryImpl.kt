package com.example.coroutinefundamentals.data.repository

import com.example.coroutinefundamentals.data.model.Post
import com.example.coroutinefundamentals.data.remote.network.ApiService
import com.example.coroutinefundamentals.utils.ErrorHandling
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PostRepositoryImpl @Inject constructor(
    private val api: ApiService // Hilt gets this from the NetworkModule we just made
) : PostRepository {
    override  fun getPosts():  Flow<ErrorHandling<List<Post>>>  = flow {
        emit(ErrorHandling.Loading)

        try {
            val posts = api.getPosts()
            emit(ErrorHandling.Success(posts))
        } catch (e: Exception) {
            emit(ErrorHandling.Error(e.message ?: "Unknown error"))
        }

}
}
