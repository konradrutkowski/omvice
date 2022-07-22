package com.krutkowski.omvice

import android.app.PictureInPictureParams
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.exoplayer2.video.VideoSize
import com.krutkowski.omvice.streams.StreamsListScreen
import com.krutkowski.omvice.ui.VideoScreen
import com.krutkowski.omvice.ui.theme.OmviceTheme

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            OmviceTheme {
                NavigationComponent(onVideoResize = ::setupPip, navController = navController)
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                ) {
//                    VideoScreen("https://www.pexels.com/video/6507676/download/", ::setupPip)
//                }
            }
        }
    }

    private fun setupPip(videoSize: VideoSize) {
        val aspectRatio = Rational(videoSize.width, videoSize.height)
        val pictureInPictureParams = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRatio)
            .setAutoEnterEnabled(true)
            .setSeamlessResizeEnabled(true)
            .build()
        setPictureInPictureParams(pictureInPictureParams)
    }
}


@Composable
fun NavigationComponent(
    onVideoResize: (videoSize: VideoSize) -> Unit,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "streams"
    ) {
        composable("streams") {
            StreamsListScreen(navController)
        }
        composable(
            "stream/{urlId}",
            arguments = listOf(navArgument("urlId") { type = NavType.StringType })
        ) {
            val videoUrl = it.arguments?.getString("urlId") ?: return@composable
            VideoScreen(videoUrl, onVideoResize)
        }
    }
}

val videoURL = "https://www.pexels.com/video/2795405/download/"

