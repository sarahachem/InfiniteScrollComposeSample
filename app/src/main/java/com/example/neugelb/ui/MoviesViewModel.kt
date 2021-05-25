package com.example.neugelb.ui

import android.app.Application
import android.net.ConnectivityManager
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neugelb.BuildConfig
import com.example.neugelb.apis.TMDBApi
import com.example.neugelb.apis.checkSuccessful
import com.example.neugelb.model.InfoAndCredits
import com.example.neugelb.model.MovieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesViewModel(
    app: Application,
    private val tmdbApi: TMDBApi,
    private val connectivityManager: ConnectivityManager
) : AndroidViewModel(app) {

    var errorLiveData = mutableLiveDataOf("")
    var isLoadingMoviesLiveData = mutableLiveDataOf(true)
    var isLoadingMovieInfoLiveData = mutableLiveDataOf(false)
    var moviesLiveData = mutableLiveDataOf<List<MovieResult>?>(null)
    var foundItemsLiveData = mutableLiveDataOf<List<MovieResult>?>(null)
    var shouldScrollUpLiveData = mutableLiveDataOf(false)
    var currentPage = 0
    var totalNumberOfPages = 1
    val movieCreditsAndInfoLiveData = mutableLiveDataOf<InfoAndCredits?>(null)
    val playTrailerLiveData = mutableLiveDataOf<String?>(null)

    suspend fun refresh() {
        moviesLiveData = mutableLiveDataOf(emptyList())
        currentPage = 0
        totalNumberOfPages = 1
        fetchMovies()
    }

    suspend fun fetchMovies() {
        val movies = moviesLiveData.value?.toMutableList() ?: mutableListOf()
        if (currentPage < totalNumberOfPages) {
            withContext(Dispatchers.IO) {
                if (hasInternetConnection()) {
                    if (currentPage == 0) isLoadingMoviesLiveData.postValue(true)
                    kotlin.runCatching {
                        tmdbApi.getMovies(BuildConfig.TMDB_API_KEY, currentPage + 1).execute()
                            .checkSuccessful()
                    }.onFailure {
                        errorLiveData.postValue(it.message)
                    }.onSuccess {
                        val reponse = it.body()
                        totalNumberOfPages = reponse?.totalPages ?: 1
                        currentPage = reponse?.page ?: 0
                        reponse?.results?.let { newMovies ->
                            movies.addAll(newMovies)
                            moviesLiveData.postValue(movies)
                        }
                    }
                } else {
                    errorLiveData.postValue(" No internet connection")
                }
            }
        }
        if (isLoadingMoviesLiveData.value == true) {
            isLoadingMoviesLiveData.postValue(false)
        }
    }

    //TODO: post live data to stop if in the middle of loading
    @VisibleForTesting
    fun hasInternetConnection() = connectivityManager.activeNetwork != null

    suspend fun onMovieClicked(movieEntry: MovieResult) {
        if (hasInternetConnection()) {
            kotlin.runCatching {
                getMovieInfoAndCredits(movieEntry)
            }.onSuccess {
                isLoadingMovieInfoLiveData.postValue(false)
            }.onFailure {
                isLoadingMovieInfoLiveData.postValue(false)
                errorLiveData.postValue(it.message)
            }
        } else {
            movieCreditsAndInfoLiveData.postValue(null)
            errorLiveData.postValue("no internet connection")
        }
    }

    private suspend fun getMovieInfoAndCredits(movieEntry: MovieResult) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                tmdbApi
                    .getMovieInfoAndCredits(movieEntry.id, BuildConfig.TMDB_API_KEY)
                    .execute().checkSuccessful()
            }.onFailure {
                errorLiveData.postValue(it.message)
            }.onSuccess {
                movieCreditsAndInfoLiveData.postValue(it.body())
            }
        }
    }

    fun filter(query: String?) {
        foundItemsLiveData.postValue(null)
        shouldScrollUpLiveData.postValue(true)
        query?.takeIf { it.isNotEmpty() }?.let {
            val result = moviesLiveData.value?.filter { entity ->
                entity.filter(query)
            }
            foundItemsLiveData.postValue(result)
        }
    }

    fun selectMovie(movieResult: MovieResult) {
        foundItemsLiveData.postValue(listOf(movieResult))
    }

    fun isLoadingInfo(isLoading: Boolean) {
        if (hasInternetConnection()) isLoadingMovieInfoLiveData.postValue(isLoading)
        else isLoadingMovieInfoLiveData.postValue(false)
    }

    fun playTrailer(key: String) {
        //only youtube videos are supported for now
        playTrailerLiveData.postValue(key.toYoutubeLink())
    }
}

class MoviesViewModelFactory(
    private val app: Application,
    private val tmdbApi: TMDBApi,
    private val connectivityManager: ConnectivityManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (MoviesViewModel(app, tmdbApi, connectivityManager) as T)
    }
}

fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> =
    MutableLiveData<T>().apply { this.value = value }

private fun String.toYoutubeLink(): String {
    return "https://youtube.com/watch?v=$this"
}