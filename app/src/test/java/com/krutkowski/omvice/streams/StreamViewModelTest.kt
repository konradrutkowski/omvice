package com.krutkowski.omvice.streams

import com.krutkowski.omvice.CoroutinesTestRule
import com.krutkowski.omvice.data.StreamMetadata
import com.krutkowski.omvice.data.stream.StreamDataSource
import com.krutkowski.omvice.util.RepositoryResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class StreamsViewModelTest {

    @Rule
    @JvmField
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var streamRepository: StreamDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState isLoading values should be correct when setup called`() = runTest {
        /* Given */
        val stream = StreamMetadata("a", "b", "c", "d")
        val streams = listOf(stream)
        val streamsFlow = MutableSharedFlow<List<StreamMetadata>>()
        val expectedIsLoadingValues = listOf(false, true, false)
        /* When */
        Mockito.`when`(streamRepository.observeStreams()).thenReturn(streamsFlow)
        Mockito.`when`(streamRepository.loadStreams()).thenReturn(RepositoryResult.Success(streams))
        val viewModel by lazy { StreamsViewModel(streamRepository) }
        val actualValues = mutableListOf<StreamsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(actualValues)
        }
        viewModel.loadStreams()
        /* Then */
        val actualIsLoadingValues = actualValues.map { it.isLoading }
        assertEquals(expectedIsLoadingValues, actualIsLoadingValues)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState error values should be correct when setup called`() = runTest {
        /* Given */
        val stream = StreamMetadata("a", "b", "c", "d")
        val streams = listOf(stream)
        val streamsFlow = MutableSharedFlow<List<StreamMetadata>>()
        val expectedErrorValues =
            listOf(StreamsError.NoError, StreamsError.NoError, StreamsError.NoError)
        /* When */
        Mockito.`when`(streamRepository.observeStreams()).thenReturn(streamsFlow)
        Mockito.`when`(streamRepository.loadStreams()).thenReturn(RepositoryResult.Success(streams))
        val viewModel by lazy { StreamsViewModel(streamRepository) }
        val actualValues = mutableListOf<StreamsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(actualValues)
        }
        viewModel.loadStreams()
        /* Then */
        val actualErrorValues = actualValues.map { it.error }
        assertEquals(expectedErrorValues, actualErrorValues)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `error values should be correct when refreshStreams called and result success`() = runTest {
        /* Given */
        val stream = StreamMetadata("a", "b", "c", "d")
        val streams = listOf(stream)
        val streamsFlow = MutableSharedFlow<List<StreamMetadata>>()
        val expectedErrorValues =
            listOf(StreamsError.NoError, StreamsError.NoError, StreamsError.NoError)
        /* When */
        Mockito.`when`(streamRepository.observeStreams()).thenReturn(streamsFlow)
        Mockito.`when`(streamRepository.loadStreams())
            .thenReturn(RepositoryResult.Success(streams))
        val viewModel by lazy { StreamsViewModel(streamRepository) }
        val actualValues = mutableListOf<StreamsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(actualValues)
        }
        viewModel.refreshStreams()
        /* Then */
        val actualErrorValues = actualValues.map { it.error }
        assertEquals(expectedErrorValues, actualErrorValues)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `error values should be correct when refreshStreams called and result error`() = runTest {
        /* Given */
        val streamsFlow = MutableSharedFlow<List<StreamMetadata>>()
        val responseMessage = "Failed"
        val expectedErrorValues = listOf(
            StreamsError.NoError,
            StreamsError.NoError,
            StreamsError.RemoteError(responseMessage)
        )
        /* When */
        Mockito.`when`(streamRepository.observeStreams()).thenReturn(streamsFlow)
        Mockito.`when`(streamRepository.loadStreams())
            .thenReturn(RepositoryResult.Error(responseMessage))
        val viewModel by lazy { StreamsViewModel(streamRepository) }
        val actualValues = mutableListOf<StreamsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(actualValues)
        }
        viewModel.refreshStreams()
        /* Then */
        val actualErrorValues = actualValues.map { it.error }
        assertEquals(expectedErrorValues, actualErrorValues)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `streamItems should be correct when streamId saved`() = runTest {
        val stream = StreamMetadata("a", "b", "c", "d")
        val streams = listOf(stream)
        val streamsFlow = MutableSharedFlow<List<StreamMetadata>>()
        val expectedStreamItems = listOf(stream)
        /* When */
        Mockito.`when`(streamRepository.observeStreams()).thenReturn(streamsFlow)
        Mockito.`when`(streamRepository.loadStreams()).thenReturn(RepositoryResult.Success(streams))
        val viewModel by lazy { StreamsViewModel(streamRepository) }
        val actualValues = mutableListOf<StreamsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(actualValues)
        }
        viewModel.loadStreams()
        streamsFlow.emit(streams)
        /* Then */
        val actualStreamItems = (actualValues[3] as StreamsUiState.HasStreams).streamItems
        assertEquals(expectedStreamItems, actualStreamItems)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `streamItems should be correct when streamId not saved`() = runTest {
        val stream = StreamMetadata("a", "b", "c", "d")
        val streams = listOf(stream)
        val streamsFlow = MutableSharedFlow<List<StreamMetadata>>()
        val expectedStreamItems = listOf(stream)
        /* When */
        Mockito.`when`(streamRepository.observeStreams()).thenReturn(streamsFlow)
        Mockito.`when`(streamRepository.loadStreams()).thenReturn(RepositoryResult.Success(streams))
        val viewModel by lazy { StreamsViewModel(streamRepository) }
        val actualValues = mutableListOf<StreamsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(actualValues)
        }
        viewModel.loadStreams()
        streamsFlow.emit(streams)
        /* Then */
        val actualStreamItems = (actualValues[3] as StreamsUiState.HasStreams).streamItems
        assertEquals(expectedStreamItems, actualStreamItems)
        job.cancel()
    }
}