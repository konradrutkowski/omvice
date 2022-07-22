package com.krutkowski.omvice.data.stream.remote

import com.krutkowski.omvice.api.OmviceApi
import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.util.RepositoryResult
import retrofit2.HttpException

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class StreamRemote(private val api: OmviceApi) : StreamRemoteDataSource {

    override suspend fun loadStreams(id: String): RepositoryResult<List<StreamMetadata>> {
        return try {
            RepositoryResult.Success(api.getStreams(id).streams)
        } catch (e: HttpException) {
            RepositoryResult.Error(e.message())
        }
    }
}