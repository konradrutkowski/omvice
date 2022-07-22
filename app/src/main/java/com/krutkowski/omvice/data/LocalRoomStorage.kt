package com.krutkowski.omvice.data

import android.content.Context
import androidx.room.Room

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class LocalRoomStorage(private val context: Context) : RoomStorage {

    private val databaseName = DATABASE_NAME
    private val room = Room.databaseBuilder(context, OmviceAppDatabase::class.java, databaseName)
        .fallbackToDestructiveMigration()
        .build()

    override fun getDatabase(): OmviceAppDatabase {
        return room
    }

    private companion object {
        const val DATABASE_NAME = "omvice_database.db"
    }
}