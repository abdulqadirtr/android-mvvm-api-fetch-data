package com.example.coroutinefundamentals

import com.example.coroutinefundamentals.data.model.Post
import com.example.coroutinefundamentals.data.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class FakePostRepository(
    private val shouldFail: Boolean = false
) : PostRepository {

    override fun getPosts(): Flow<List<Post>> {
        if (shouldFail) {
            throw RuntimeException("Network error")
        }

        return listOf(
            Post(1, id= 123, "Title 1", "Body 1"),
            Post(2, id= 342, "Title 2", "Body 2")
        ) as Flow<List<Post>>
    }
}
