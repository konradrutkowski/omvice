package com.krutkowski.omvice.data.stream.remote

import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.util.RepositoryResult

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
interface StreamRemoteDataSource {
    suspend fun loadStreams(id: String): RepositoryResult<List<StreamMetadata>>
}