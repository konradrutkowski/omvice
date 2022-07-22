package com.krutkowski.omvice.streams

import com.krutkowski.omvice.data.StreamMetadata

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
sealed interface StreamsUiState {
    val isLoading: Boolean
    val error: StreamsError

    data class NoStreams(override val isLoading: Boolean, override val error: StreamsError) :
        StreamsUiState

    data class HasStreams(
        override val isLoading: Boolean,
        override val error: StreamsError,
        val streamItems: List<StreamMetadata>
    ) : StreamsUiState
}

sealed class StreamsError {
    object NoError : StreamsError()
    data class RemoteError(val reason: String) : StreamsError()
}