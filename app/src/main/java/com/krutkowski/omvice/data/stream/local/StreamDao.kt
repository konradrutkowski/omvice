package com.krutkowski.omvice.data.stream.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krutkowski.omvice.data.StreamMetadata
import kotlinx.coroutines.flow.Flow

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
@Dao
interface StreamDao {
    @Query("SELECT * FROM streams")
    fun observeStreams(): Flow<List<StreamMetadata>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStreams(streams: List<StreamMetadata>)

    @Query("DELETE FROM streams")
    fun deleteStreams()
}