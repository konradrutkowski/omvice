package com.krutkowski.omvice.streams

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krutkowski.omvice.data.stream.StreamDataSource
import com.krutkowski.omvice.util.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class StreamsViewModel(private val streamRepository: StreamDataSource) : ViewModel() {

    private val viewModelState = MutableStateFlow(StreamsViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun loadStreams() {
        refreshStreams()
        observeStreams()
    }

    private fun observeStreams() {
        viewModelScope.launch {
            streamRepository.observeStreams().collect() { streams ->
                viewModelState.update { it.copy(streamItems = streams) }
            }
        }
    }

    fun refreshStreams() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = streamRepository.loadStreams()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error ->
                        it.copy(isLoading = false, error = StreamsError.RemoteError(result.error))
                }
            }
        }
    }

    fun clearError() {
        viewModelState.update { it.copy(error = StreamsError.NoError) }
    }
}