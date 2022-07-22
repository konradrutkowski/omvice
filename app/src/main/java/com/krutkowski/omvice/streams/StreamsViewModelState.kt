package com.krutkowski.omvice.streams

import com.krutkowski.omvice.data.StreamMetadata

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
data class StreamsViewModelState(
    val isLoading: Boolean = false,
    val error: StreamsError = StreamsError.NoError,
    val streamItems: List<StreamMetadata> = emptyList()
) {
    fun toUiState(): StreamsUiState = if (streamItems.isEmpty()) {
        StreamsUiState.NoStreams(isLoading, error)
    } else {
        StreamsUiState.HasStreams(isLoading, error, streamItems)
    }
}