package com.krutkowski.omvice.data.streams

import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.data.stream.StreamDataSource
import com.krutkowski.omvice.data.stream.StreamRepository
import com.krutkowski.omvice.data.stream.local.StreamLocalDataSource
import com.krutkowski.omvice.data.stream.remote.StreamRemoteDataSource
import com.krutkowski.omvice.util.RepositoryResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class StreamRepositoryTest {

    @Mock
    private lateinit var local: StreamLocalDataSource

    @Mock
    private lateinit var remote: StreamRemoteDataSource
    private lateinit var repository: StreamDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = StreamRepository(local, remote)
    }

    @Test
    fun `observeStreams should call observeStreams on local`() {
        /* When */
        repository.observeStreams()
        /* Then */
        Mockito.verify(local, Mockito.times(1)).observeStreams()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadStreams should call loadStreams on remote`() = runTest {
        /* When */
        repository.loadStreams()
        /* Then */
        Mockito.verify(remote, Mockito.times(1)).loadStreams(anyString())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadStreams should call deleteStreams and saveStreams on local when remote result success`() =
        runTest {
            val streams = listOf<StreamMetadata>()
            /* When */
            Mockito.`when`(remote.loadStreams(anyString()))
                .thenReturn(RepositoryResult.Success(streams))
            repository.loadStreams()
            /* Then */
            Mockito.verify(local, Mockito.times(1)).deleteStreams()
            Mockito.verify(local, Mockito.times(1)).saveStreams(streams)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadStreams should not call deleteStreams and saveStreams on local when remote result error`() =
        runTest {
            val errorMessage = "Load stream failed"
            /* When */
            Mockito.`when`(remote.loadStreams(anyString()))
                .thenReturn(RepositoryResult.Error(errorMessage))
            repository.loadStreams()
            /* Then */
            Mockito.verify(local, Mockito.times(0)).deleteStreams()
            Mockito.verify(local, Mockito.times(0)).saveStreams(any())
        }
}