package com.example.coroutinefundamentals.ui.viewmodels

import app.cash.turbine.test
import com.example.coroutinefundamentals.data.model.Post
import com.example.coroutinefundamentals.data.repository.PostRepository
import com.example.coroutinefundamentals.ui.repository.FakePostRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class PostViewModelTest {


    // 🔧 Use TestDispatcher instead of Main (Android) Dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    //This one is used for android UI testing
   // private val dispatcherMain = Dispatchers.Main

    private lateinit var fakeRepository: FakePostRepository
    private lateinit var viewModel: PostViewModel



    @Before
    fun setup() {
        // Set Main dispatcher to test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Create fake repository
        fakeRepository = FakePostRepository()

        // Create ViewModel with fake repository
        viewModel = PostViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ============================================================
    // 🎯 PRIORITY 1: Business Logic & State Changes
    // ============================================================

    /**
     * TEST 1: Success case - Posts loaded correctly
     *
     * What it tests:
     * - ViewModel receives data from repository
     * - State is updated with posts
     * - Loading is set to false
     * - Error is empty
     * - Titles are transformed to uppercase
     */
    @Test
    fun `loadPosts - success - updates state with posts and transforms titles to uppercase`() =
        runTest {
            // When: Repository emits success
            fakeRepository.emitSuccess(FakePostRepository.dummyPosts)

            // Then: State should have transformed posts
            viewModel.uiState.test {
                val state = awaitItem()

                assertThat(state.isLoading).isFalse()
                assertThat(state.posts).hasSize(3)
                assertThat(state.error).isEmpty()

                // ✅ Check transformation (title uppercase)
                assertThat(state.posts[0].title).isEqualTo("FIRST POST TITLE")
                assertThat(state.posts[1].title).isEqualTo("SECOND POST TITLE")
                assertThat(state.posts[2].title).isEqualTo("THIRD POST TITLE")

                // ✅ Original data should remain unchanged (only title transformed)
                assertThat(state.posts[0].body).isEqualTo("This is the body of first post")
            }
        }


    @Test
    fun `loadPosts - error - updates state with error message`() = runTest {
        // When: Repository emits error
        fakeRepository.emitError("Network error: 404 Not Found")

        // Then: Error should be in state
        viewModel.uiState.test {
            val state = awaitItem()

            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isEqualTo("Network error: 404 Not Found")
            assertThat(state.posts).isEmpty()
        }
    }



    /**
     * TEST 3: Empty result - Handles gracefully
     *
     * What it tests:
     * - App doesn't crash with empty list
     * - Empty list is shown properly
     */
    @Test
    fun `loadPosts - success but empty list - handles gracefully`() = runTest {
        // When: Repository returns empty list
        fakeRepository.emitSuccess(FakePostRepository.emptyPosts)

        // Then: Should handle empty list
        viewModel.uiState.test {
            val state = awaitItem()

            assertThat(state.posts).isEmpty()
            assertThat(state.error).isEmpty()
            assertThat(state.isLoading).isFalse()
        }
    }


    /**
     * TEST 4: Loading state - Shows loading
     */
    @Test
    fun `loadPosts - loading - shows loading state`() = runTest {
        // When: Repository emits loading
        fakeRepository.emitLoading()

        // Then: Loading should be true
        viewModel.uiState.test {
            val state = awaitItem()

            assertThat(state.isLoading).isTrue()
            assertThat(state.posts).isEmpty()
            assertThat(state.error).isEmpty()
        }
    }


    // ============================================================
    // 🎯 PRIORITY 2: Edge Cases - Boundary Conditions
    // ============================================================

    /**
     * TEST 5: getPostById - Returns correct post
     */
    @Test
    fun `getPostById - post exists - returns correct post`() = runTest {
        // Given: Load posts first
        fakeRepository.emitSuccess(FakePostRepository.dummyPosts)

        viewModel.uiState.test {
            awaitItem() // Wait for state update

            // When: Get post by ID
            val post = viewModel.getPostById(2)

            // Then: Should return the correct post with transformed title
            assertThat(post).isNotNull()
            assertThat(post?.id).isEqualTo(2)
            assertThat(post?.title).isEqualTo("SECOND POST TITLE") // Uppercase
        }
    }

    /**
     * TEST 6: getPostById - Post doesn't exist - returns null
     */
    @Test
    fun `getPostById - post does not exist - returns null`() = runTest {
        // Given: Load posts
        fakeRepository.emitSuccess(FakePostRepository.dummyPosts)

        viewModel.uiState.test {
            awaitItem()

            // When: Get non-existent post
            val post = viewModel.getPostById(999)

            // Then: Should return null
            assertThat(post).isNull()
        }
    }

    /**
     * TEST 7: getPostById - Empty list - returns null
     */
    @Test
    fun `getPostById - empty list - returns null`() = runTest {
        // Given: Empty list
        fakeRepository.emitSuccess(emptyList())

        viewModel.uiState.test {
            awaitItem()

            // When: Try to get any post
            val post = viewModel.getPostById(1)

            // Then: Should return null
            assertThat(post).isNull()
        }
    }

    // ============================================================
    // 🎯 PRIORITY 3: Initial State (ViewModel calls loadPosts in init)
    // ============================================================

    /**
     * TEST 8: Initial state after loading
     *
     * Note: ViewModel calls loadPosts() in init block,
     * so we test the state after automatic loading
     */
    @Test
    fun `init - automatically loads posts on creation`() = runTest {
        // Given: Fresh ViewModel with data ready
        val freshFakeRepo = FakePostRepository()
        freshFakeRepo.emitSuccess(FakePostRepository.dummyPosts)

        // When: ViewModel is created (loadPosts called in init)
        val freshViewModel = PostViewModel(freshFakeRepo)

        // Then: Should have loaded data automatically
        freshViewModel.uiState.test {
            val state = awaitItem()

            assertThat(state.posts).hasSize(3)
            assertThat(state.isLoading).isFalse()
            assertThat(freshFakeRepo.getCallCount()).isEqualTo(1)
        }
    }

    // ============================================================
    // 🎯 PRIORITY 4: User Actions (onRefresh, onRetry)
    // ============================================================

    /**
     * TEST 9: onRefresh - calls loadPosts again
     */
    @Test
    fun `onRefresh - reloads posts from repository`() = runTest {
        // Given: Initial load
        fakeRepository.emitSuccess(listOf(Post(1, 1, "Old Title", "Body")))

        viewModel.uiState.test {
            awaitItem() // Initial state

            // When: User refreshes
            fakeRepository.reset()
            fakeRepository.emitSuccess(listOf(Post(1, 1, "New Title", "Body")))
            viewModel.onRefresh()

            // Then: Should have new data
            val state = awaitItem()
            assertThat(state.posts[0].title).isEqualTo("NEW TITLE")
            assertThat(fakeRepository.getCallCount()).isEqualTo(1) // Called after reset
        }
    }

    /**
     * TEST 10: onRetry - retries after error
     */
    @Test
    fun `onRetry - after error - successfully loads posts`() = runTest {
        // Given: Initial error
        fakeRepository.emitError("Network error")

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertThat(errorState.error).isNotEmpty()

            // When: User retries with success data
            fakeRepository.emitSuccess(FakePostRepository.dummyPosts)
            viewModel.onRetry()

            // Then: Should load successfully
            val successState = awaitItem()
            assertThat(successState.posts).hasSize(3)
            assertThat(successState.error).isEmpty()
        }
    }

    // ============================================================
    // 🎯 PRIORITY 5: Transformation Logic
    // ============================================================

    /**
     * TEST 11: Transformation - lowercase input becomes uppercase
     */
    @Test
    fun `loadPosts - transforms lowercase titles to uppercase`() = runTest {
        // Given: Posts with lowercase titles
        val lowercasePosts = listOf(
            Post(1, 1, "hello world", "body"),
            Post(1, 2, "kotlin is awesome", "body")
        )

        // When
        fakeRepository.emitSuccess(lowercasePosts)

        // Then: Titles should be uppercase
        viewModel.uiState.test {
            val state = awaitItem()

            assertThat(state.posts[0].title).isEqualTo("HELLO WORLD")
            assertThat(state.posts[1].title).isEqualTo("KOTLIN IS AWESOME")
        }
    }

    /**
     * TEST 12: Transformation - mixed case becomes uppercase
     */
    @Test
    fun `loadPosts - transforms mixed case titles to uppercase`() = runTest {
        // Given: Mixed case
        val mixedPosts = listOf(
            Post(1, 1, "HeLLo WoRLd", "body")
        )

        // When
        fakeRepository.emitSuccess(mixedPosts)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.posts[0].title).isEqualTo("HELLO WORLD")
        }
    }

    /**
     * TEST 13: Transformation - empty title handled
     */
    @Test
    fun `loadPosts - handles empty title gracefully`() = runTest {
        // Given: Post with empty title
        val emptyTitlePost = listOf(
            Post(1, 1, "", "body")
        )

        // When
        fakeRepository.emitSuccess(emptyTitlePost)

        // Then: Should not crash
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.posts[0].title).isEmpty()
        }
    }

    // ============================================================
    // 🎯 PRIORITY 6: Repository Call Verification
    // ============================================================

    /**
     * TEST 14: Verify repository is called correct number of times
     */
    @Test
    fun `loadPosts - calls repository getPosts exactly once per call`() = runTest {
        // Given: Fresh repository
        fakeRepository.reset()
        fakeRepository.emitSuccess(FakePostRepository.dummyPosts)

        // When: Load posts
        viewModel.loadPosts()

        // Then: Repository should be called once
        assertThat(fakeRepository.getCallCount()).isEqualTo(1)
    }

    /**
     * TEST 15: Multiple loadPosts calls - each calls repository
     */
    @Test
    fun `loadPosts - called multiple times - calls repository multiple times`() = runTest {
        // Given
        fakeRepository.reset()
        fakeRepository.emitSuccess(FakePostRepository.dummyPosts)

        // When: Load 3 times
        viewModel.loadPosts()
        viewModel.loadPosts()
        viewModel.loadPosts()

        // Then
        assertThat(fakeRepository.getCallCount()).isEqualTo(3)
    }



    /**
     * TEST 2: Error case - Error message shown correctly
     *
     * What it tests:
     * - ViewModel handles error from repository
     * - Error message is stored in state
     * - Posts list is empty
     * - Loading is false
     */



}