package com.krutkowski.omvice.api

import com.krutkowski.omvice.data.stream.model.StreamMetadataWrapper
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
interface OmviceApi {
    @GET("v3/{id}")
    suspend fun getStreams(@Path("id") id: String): StreamMetadataWrapper
}