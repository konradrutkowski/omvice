package com.krutkowski.omvice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
@Entity(tableName = "streams")
data class StreamMetadata(
    @PrimaryKey @ColumnInfo(name = "uuid") @SerializedName("uuid") val uuid: String,
    @ColumnInfo(name = "thumb_url") @SerializedName("thumb_url") var thumbUrl: String? = null,
    @ColumnInfo(name = "name") @SerializedName("name") var name: String? = null,
    @ColumnInfo(name = "description") @SerializedName("description") var description: String? = null,
    @ColumnInfo(name = "video_url") @SerializedName("video_url") var videoUrl: String? = null
)