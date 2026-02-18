package com.example.coroutinefundamentals.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coroutinefundamentals.ui.screen.DetailScreen
import com.example.coroutinefundamentals.ui.screen.PostsScreen

@Composable
fun AppNavigation() {

    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "posts") {

        // Post list Screen
        composable("posts") {

            PostsScreen(onPostClick = { postId ->
                navController.navigate("detail/$postId")
            }
            )
        }

        // Details list Screen
        composable(
            "detail/{postId}",
            arguments = listOf(
                navArgument("postId")
                {
                    type = NavType.IntType
                })
        )
        { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId")
            postId?.let {
                DetailScreen(postId = it.toString(), onBackClick = {
                    navController.popBackStack()
                })
            }
        }

    }


}