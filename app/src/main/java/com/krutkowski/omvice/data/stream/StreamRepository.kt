package com.krutkowski.omvice.data.stream

import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.data.stream.local.StreamLocalDataSource
import com.krutkowski.omvice.data.stream.remote.StreamRemoteDataSource
import com.krutkowski.omvice.util.RepositoryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class StreamRepository(
    private val local: StreamLocalDataSource,
    private val remote: StreamRemoteDataSource
) : StreamDataSource {

    override fun observeStreams(): Flow<List<StreamMetadata>> {
        return local.observeStreams()
    }

    override suspend fun loadStreams(): RepositoryResult<List<StreamMetadata>> {
        return remote.loadStreams(MOCK_ID).also {
            withContext(Dispatchers.IO) {
                if (it is RepositoryResult.Success) {
                    local.deleteStreams()
                    local.saveStreams(it.data)
                }
            }
        }
    }

    private companion object {
        const val MOCK_ID = "a6e335ad-6e6d-43a2-b0b9-a68e94803c36"
    }
}