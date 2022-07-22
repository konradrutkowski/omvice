package com.krutkowski.omvice.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.SpeakerNotesOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ShareCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.video.VideoSize
import com.krutkowski.omvice.ui.theme.OmviceTheme
import com.krutkowski.omvice.video.VideoUiState
import com.krutkowski.omvice.video.VideoViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
val fullUrl: (String) -> String = { "https://www.pexels.com/video/$it/download/" }

@Composable
fun VideoScreen(
    videoUrl: String,
    onVideoResize: (videoSize: VideoSize) -> Unit,
    viewModel: VideoViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect("LOAD_VIEWERS") {
        viewModel.loadViewers()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            VideoPlayer(fullUrl(videoUrl), onVideoResize)
            Row(
                modifier = Modifier.padding(top = 16.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                LiveLabel(uiState)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                val context = LocalContext.current
                BlackCircleButton(Icons.Rounded.Share) {
                    val shareIntent: Intent = ShareCompat.IntentBuilder(context)
                        .setType("text/plain")
                        .setText("Check our video ${fullUrl(videoUrl)}")
                        .setSubject("Worth to check!")
                        .intent
                    context.startActivity(shareIntent)
                }
                Spacer(modifier = Modifier.size(8.dp))
                BlackCircleButton(Icons.Rounded.SpeakerNotesOff) {}
            }
        }
    }
}

@Composable
fun BlackCircleButton(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Button(
        shape = CircleShape,
        onClick = onClick,
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.Black.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = Modifier
                .size(20.dp)
                .background(Color.Transparent),
            tint = Color.White,
        )
    }
}

@Composable
fun LiveLabel(uiState: VideoUiState) {
    Box(modifier = Modifier.clip(shape = RoundedCornerShape(bottomEnd = 12.dp, topEnd = 12.dp))) {
        Row(
            modifier = Modifier.background(Color.White.copy(alpha = 0.6f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(6.dp))
            AnimatedCounter(uiState.viewers)
            Spacer(modifier = Modifier.width(6.dp))
            AnimatedDot()
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

@Composable
fun AnimatedDot(
    modifier: Modifier = Modifier,
    circleSize: Dp = 8.dp,
    circleColor: Color = Color.Red
) {
    val circle = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = circle) {
        delay(100L)
        circle.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0.0f at 0 with LinearOutSlowInEasing
                    1.0f at 600 with LinearOutSlowInEasing
                    0.0f at 1200 with LinearOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .graphicsLayer {
                    alpha = circle.value
                }
                .background(
                    color = circleColor,
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun AnimatedCounter(count: Int) {
    val counter by animateIntAsState(targetValue = count, animationSpec = tween(1000))
    Text(text = "$counter")
}

@Composable
private fun VideoPlayer(videoUrl: String, pipSetup: (videoSize: VideoSize) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        val exoPlayer = remember(context) {
            ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.Builder()
                    .setUri(Uri.parse(videoUrl))
                    .build()
                addListener(object : Player.Listener {
                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        pipSetup(videoSize)
                    }
                })
                setMediaItem(mediaItem)
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ALL
                prepare()
            }
        }
        AndroidView(factory = { viewContext ->
            StyledPlayerView(viewContext).apply {
                player = exoPlayer
                useController = false
            }
        })
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OmviceTheme {
        VideoPlayer("") {}
    }
}