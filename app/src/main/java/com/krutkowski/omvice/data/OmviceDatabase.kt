package com.krutkowski.omvice.data

import androidx.room.RoomDatabase
import com.krutkowski.omvice.data.stream.local.StreamDao

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
abstract class OmviceDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
}