package com.krutkowski.omvice.streams

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.krutkowski.omvice.R
import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.ui.component.AppTopBar
import com.krutkowski.omvice.ui.theme.Blue500
import org.koin.androidx.compose.getViewModel

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */

@Composable
fun StreamsListScreen(
    navController: NavHostController,
    viewModel: StreamsViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadStreams() }
    StreamsScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refreshStreams() },
        onStreamClick = { navController.navigate("stream/${it.videoUrl}") },
        onErrorClear = { viewModel.clearError() },
        scaffoldState = scaffoldState
    )
}

@Composable
fun StreamsScreen(
    modifier: Modifier = Modifier,
    uiState: StreamsUiState,
    onRefreshClick: () -> Unit,
    onStreamClick: (StreamMetadata) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState
) {
    Scaffold(topBar = { TopBar() }) {
        LoadingContent(
            empty = when (uiState) {
                is StreamsUiState.HasStreams -> false
                is StreamsUiState.NoStreams -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            when (uiState) {
                is StreamsUiState.HasStreams -> StreamList(
                    streams = uiState.streamItems,
                    onStreamClick = onStreamClick,
                    modifier = modifier
                )
                is StreamsUiState.NoStreams -> StreamsEmptyState(onRefreshClick)
            }
        }

        ErrorSnackbar(uiState, onRefreshClick, onErrorClear, scaffoldState)
    }
}

@Composable
private fun StreamList(
    streams: List<StreamMetadata>,
    onStreamClick: (StreamMetadata) -> Unit,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        state = state
    ) {
        item {
            Column {
                streams.forEach {
                    StreamCard(streamItem = it, onStreamClick = onStreamClick)
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    AppTopBar(
        shape = RoundedCornerShape(0, 0, 24, 24),
        title = {
            Text(text = stringResource(id = R.string.app_name))
        }
    )
}

@Composable
private fun StreamCard(streamItem: StreamMetadata, onStreamClick: (StreamMetadata) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(id = R.color.white_smoke))
                .fillMaxWidth()
                .clickable(onClick = { onStreamClick(streamItem) })
                .padding(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .clip(CutCornerShape(8.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(streamItem.thumbUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Fit

            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = streamItem.name.orEmpty(),
                    style = MaterialTheme.typography.h5,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = streamItem.description.orEmpty(),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content
        )
    }
}

@Composable
private fun StreamsEmptyState(onRefreshClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            modifier = Modifier
                .size(128.dp)
                .padding(8.dp),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "No streams"
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = onRefreshClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Blue500)
        ) {
            Text(
                "Refresh",
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: StreamsUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState
) {
    when (val error = uiState.error) {
        is StreamsError.NoError -> {}
        is StreamsError.RemoteError -> {
            val errorMessageText =
                remember(uiState) { error.reason }
            val retryMessageText = "Retry"
            val onRefreshPostsState by rememberUpdatedState(onRefreshClick)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText, retryMessageText, scaffoldState) {
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = errorMessageText,
                    actionLabel = retryMessageText
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    onRefreshPostsState()
                }
                onErrorDismissState()
            }
        }
    }
}