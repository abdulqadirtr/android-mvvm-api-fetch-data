package com.example.coroutinefundamentals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinefundamentals.data.model.Post
import com.example.coroutinefundamentals.data.repository.PostRepository
import com.example.coroutinefundamentals.utils.ErrorHandling
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor( private val postRepository: PostRepository) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // ✅ Single source of truth for UI state
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {

            postRepository.getPosts().collect { result ->
                when (result) {
                    is ErrorHandling.Success -> {
                        // ✅ UI State holder - single source of truth
                        _uiState.value = PostUiState(
                            isLoading = false,
                            posts = result.data,
                            error = ""
                        )

                        _uiEvent.emit(UiEvent.ShowSnackbar("${result.data.size} posts loaded successfully"))
                            }

                    is ErrorHandling.Error -> {
                        _uiState.value = PostUiState(
                            isLoading = false,
                            error = result.message
                        )
                        // ✅ Emit error event
                        _uiEvent.emit(UiEvent.ShowSnackbar(result.message))
                    }

                    is ErrorHandling.Loading -> {

                        _uiState.value = PostUiState(isLoading = true)
                    }
                    }
                }

            }
        }



// ✅ User actions
fun onRefresh() {
    loadPosts()
}

fun onRetry() {
    loadPosts()
}

    // ✅ UI State holder - single source of truth
    data class PostUiState(
        val isLoading: Boolean = false,
        val posts: List<Post> = emptyList(),
        val error: String = ""
    )

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }

}