package com.krutkowski.omvice.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class VideoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState.asStateFlow()

    fun loadViewers() {
        viewModelScope.launch {
            while (isActive) {
                val viewers = Random.nextInt(0, 1000)
                _uiState.update { it.copy(viewers = viewers) }
                delay(5000)
            }
        }
    }

    fun hideCustomControllers() {
        _uiState.update { it.copy(shouldShowCustomControllers = false) }
    }

    fun showCustomControllers() {
        _uiState.update { it.copy(shouldShowCustomControllers = true) }
    }
}


data class VideoUiState(val viewers: Int = 0, val shouldShowCustomControllers: Boolean = true)