package com.example.neugelb

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.neugelb.model.MoviesResponse
import com.example.neugelb.ui.MoviesViewModel
import com.google.gson.GsonBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import java.io.FileReader
import com.example.neugelb.apis.TMDBApi
import com.example.neugelb.model.MovieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class MoviesViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var app: Application

    @Mock
    private lateinit var api: TMDBApi

    @Mock
    private lateinit var connectionManager: ConnectivityManager

    @InjectMocks
    private lateinit var viewModel: MoviesViewModel

    @Mock
    lateinit var observer: Observer<in List<MovieResult>?>

    @Mock
    lateinit var network: Network

    private val movies: MoviesResponse = GsonBuilder().create()
        .fromJson(FileReader("./MoviesResponseJson"), MoviesResponse::class.java)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
        `when`(api.getMovies(anyString(), anyInt(), anyString())).thenReturn(
            TestCall.buildSuccess(movies)
        )
        `when`(connectionManager.activeNetwork).thenReturn(network)
        viewModel = MoviesViewModel(app, api, connectionManager)
        viewModel.moviesLiveData.observeForever(observer)
    }

    @Test
    fun testFetchMovies() {
        runBlocking {
            launch(Dispatchers.Main) {
                viewModel.fetchMovies()
                assertEquals(viewModel.moviesLiveData.value, movies.results)
            }
        }
    }

    @Test
    fun testFilterMovies() {
        runBlocking {
            launch(Dispatchers.Main) {
                viewModel.fetchMovies()
                viewModel.filter("a")
                assertEquals(viewModel.foundItemsLiveData.value, listOf(movies.results[2]))
            }
        }
    }

    @Test
    fun testFilterWithSpaceMovies() {
        runBlocking {
            launch(Dispatchers.Main) {
                viewModel.fetchMovies()
                viewModel.filter("a ")
                assertEquals(viewModel.foundItemsLiveData.value, listOf(movies.results[2]))
            }
        }
    }

    @Test
    fun testFilterNoMovie() {
        runBlocking {
            launch(Dispatchers.Main) {
                viewModel.fetchMovies()
                viewModel.filter("x")
                assertEquals(viewModel.foundItemsLiveData.value, emptyList<MovieResult>())
            }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}
