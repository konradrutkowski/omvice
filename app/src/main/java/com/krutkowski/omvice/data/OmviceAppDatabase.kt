package com.krutkowski.omvice.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krutkowski.omvice.data.stream.local.StreamDao

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
@Database(entities = [StreamMetadata::class], version = 1)
abstract class OmviceAppDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
}