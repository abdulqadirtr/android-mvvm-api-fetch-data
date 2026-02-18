package com.example.coroutinefundamentals.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coroutinefundamentals.ui.component.PostCard
import com.example.coroutinefundamentals.ui.viewmodels.PostViewModel


@Composable
fun PostsScreen(viewModel: PostViewModel = hiltViewModel(),
                onPostClick: (Int) -> Unit = {} ){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.error.isNotEmpty() ->
            {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.onRetry() }) {
                        Text("Retry")
                    }
                }

            }
            else->{
                LazyColumn {

                    items(uiState.posts) { post ->
                        PostCard(
                            post = post,
                            onClickCard = { onPostClick(post.id) }  // ✅ ADD THIS LINE
                        )

                    }
                }
            }

        }
    }
}