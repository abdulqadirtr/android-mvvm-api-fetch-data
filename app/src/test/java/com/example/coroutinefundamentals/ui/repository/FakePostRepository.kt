package com.example.coroutinefundamentals.ui.repository

import com.example.coroutinefundamentals.data.model.Post
import com.example.coroutinefundamentals.data.repository.PostRepository
import com.example.coroutinefundamentals.utils.ErrorHandling
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakePostRepository : PostRepository{


    // 🎯 Control what the repository returns
    private val _postsFlow = MutableSharedFlow<ErrorHandling<List<Post>>>(
        replay = 1,
        extraBufferCapacity = 1
    )

    // 📊 Track what was called (for verification)
    private var getPostsCallCount = 0

    override fun getPosts(): Flow<ErrorHandling<List<Post>>> {
        getPostsCallCount++
        return _postsFlow.asSharedFlow()
    }

    // 🎬 Helper functions to simulate different scenarios

    /**
     * Simulate successful API call with posts
     */
    suspend fun emitSuccess(posts: List<Post>) {
        _postsFlow.emit(ErrorHandling.Success(posts))
    }

    /**
     * Simulate error from API
     */
    suspend fun emitError(message: String) {
        _postsFlow.emit(ErrorHandling.Error(message))
    }

    /**
     * Simulate loading state
     */
    suspend fun emitLoading() {
        _postsFlow.emit(ErrorHandling.Loading)  // ✅ Changed from Loading() to Loading
    }


    /**
     * Get call count - useful for verification
     */
    fun getCallCount(): Int = getPostsCallCount

    /**
     * Reset for next test
     */
    fun reset() {
        getPostsCallCount = 0
    }



    // 💡 Predefined test data
    companion object {
        val dummyPosts = listOf(
            Post(
                userId = 1,
                id = 1,
                title = "First Post Title",
                body = "This is the body of first post"
            ),
            Post(
                userId = 1,
                id = 2,
                title = "Second Post Title",
                body = "This is the body of second post"
            ),
            Post(
                userId = 2,
                id = 3,
                title = "Third Post Title",
                body = "This is the body of third post"
            )
        )

        val emptyPosts = emptyList<Post>()

        const val sampleErrorMessage = "Network error: Unable to connect"
    }




    }