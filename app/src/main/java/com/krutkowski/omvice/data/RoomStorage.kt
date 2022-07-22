package com.krutkowski.omvice.data

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
interface RoomStorage {
    fun getDatabase(): OmviceAppDatabase
}