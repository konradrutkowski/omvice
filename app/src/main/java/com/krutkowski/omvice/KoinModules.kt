package com.krutkowski.omvice

import com.krutkowski.omvice.api.networkModule
import com.krutkowski.omvice.data.LocalRoomStorage
import com.krutkowski.omvice.data.RoomStorage
import com.krutkowski.omvice.data.stream.StreamDataSource
import com.krutkowski.omvice.data.stream.StreamRepository
import com.krutkowski.omvice.data.stream.local.StreamLocal
import com.krutkowski.omvice.data.stream.remote.StreamRemote
import com.krutkowski.omvice.streams.StreamsViewModel
import com.krutkowski.omvice.video.VideoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class KoinModules {

    private val viewModelsModule = module {
        viewModel { StreamsViewModel(get()) }
        viewModel { VideoViewModel() }
    }

    private val repositoriesModule = module {
        single<StreamDataSource> {
            val local = StreamLocal(get())
            val remote = StreamRemote(get())
            StreamRepository(local, remote)
        }
    }

    private val storagesModule = module {
        single<RoomStorage> {
            LocalRoomStorage(androidApplication())
        }
    }

    fun getAllModules() = listOf(
        storagesModule,
        networkModule,
        repositoriesModule,
        viewModelsModule
    )
}

