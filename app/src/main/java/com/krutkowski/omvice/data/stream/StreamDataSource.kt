package com.krutkowski.omvice.data.stream

import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.util.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
interface StreamDataSource {
    fun observeStreams(): Flow<List<StreamMetadata>>

    suspend fun loadStreams(): RepositoryResult<List<StreamMetadata>>
}