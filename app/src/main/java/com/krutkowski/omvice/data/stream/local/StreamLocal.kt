package com.krutkowski.omvice.data.stream.local

import com.krutkowski.omvice.data.RoomStorage
import com.krutkowski.omvice.data.StreamMetadata
import kotlinx.coroutines.flow.Flow

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class StreamLocal(private val roomStorage: RoomStorage) : StreamLocalDataSource {

    override fun observeStreams(): Flow<List<StreamMetadata>> {
        return roomStorage.getDatabase().streamDao().observeStreams()
    }

    override fun saveStreams(streams: List<StreamMetadata>) {
        roomStorage.getDatabase().streamDao().insertStreams(streams)
    }

    override fun deleteStreams() {
        roomStorage.getDatabase().streamDao().deleteStreams()
    }
}