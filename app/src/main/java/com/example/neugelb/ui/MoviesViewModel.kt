package com.example.neugelb.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.neugelb.apis.ApiBuilders
import com.example.neugelb.BuildConfig
import com.example.neugelb.apis.checkSuccessful
import com.example.neugelb.model.InfoAndCredits
import com.example.neugelb.model.MovieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(
    private val app: Application,
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

    init {
        fetchMovies()
    }

    fun refresh() {
        moviesLiveData = mutableLiveDataOf(emptyList())
        currentPage = 0
        totalNumberOfPages = 1
        fetchMovies()
    }

    fun fetchMovies() {
        val movies = moviesLiveData.value?.toMutableList() ?: mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            if (currentPage < totalNumberOfPages) {
                withContext(Dispatchers.IO) {
                    if (hasInternetConnection()) {
                        if (currentPage == 0) {
                            isLoadingMoviesLiveData.postValue(true)
                        }
                        kotlin.runCatching {
                            ApiBuilders.getTMDBApi().getMovies(
                                BuildConfig.TMDB_API_KEY, currentPage + 1
                            ).execute().checkSuccessful()
                        }.onFailure {
                            errorLiveData.postValue(it.message)
                        }.onSuccess {
                            totalNumberOfPages = it.body()?.totalPages ?: 1
                            currentPage = it.body()?.page ?: 0
                            it.body()?.results?.let { newMovies ->
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
    }

    private fun hasInternetConnection(): Boolean {
        val activeNetwork =
            (app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting;
    }

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
                ApiBuilders.getTMDBApi()
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
}

class MoviesViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (MoviesViewModel(app) as T)
    }
}

fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> =
    MutableLiveData<T>().apply { this.value = value }
