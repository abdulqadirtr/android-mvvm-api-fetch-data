package com.example.coroutinefundamentals

import android.os.Bundle
import android.widget.Button
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coroutinefundamentals.ui.theme.CoroutineFundamentalsTheme
import com.example.coroutinefundamentals.ui.viewmodels.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutineFundamentalsTheme {


                val viewModel = hiltViewModel<PostViewModel>()


                // 2️⃣ Collect SharedFlow EVENTS
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is PostViewModel.UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }

                /**
                 * 3️⃣ Use Scaffold with SnackbarHost only for sharedFlow demo

                 */
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->

                    PostListScreen(posts = uiState)
                }
            }
        }

    }
}



@Composable
fun PostListScreen(posts: PostViewModel.PostUiState) {

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            posts.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
            posts.error.isNotEmpty() ->
            {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = posts.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text("Retry")
                    }
                }

            }
            else->{
                LazyColumn {
                    items(posts.posts) { post ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }

        }
    }
}