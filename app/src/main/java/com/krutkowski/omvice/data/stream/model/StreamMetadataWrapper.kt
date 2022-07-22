package com.krutkowski.omvice.data.stream.model

import com.google.gson.annotations.SerializedName
import com.krutkowski.omvice.data.StreamMetadata

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
data class StreamMetadataWrapper(@SerializedName("streams") var streams: List<StreamMetadata>)