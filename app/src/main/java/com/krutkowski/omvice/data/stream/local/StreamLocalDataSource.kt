package com.krutkowski.omvice.data.stream.local

import com.krutkowski.omvice.data.StreamMetadata
import kotlinx.coroutines.flow.Flow

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
interface StreamLocalDataSource {
    fun observeStreams(): Flow<List<StreamMetadata>>
    fun saveStreams(streams: List<StreamMetadata>)
    fun deleteStreams()
}