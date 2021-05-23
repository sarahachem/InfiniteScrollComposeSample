package com.example.neugelb.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.neugelb.ApiBuilders
import com.example.neugelb.BuildConfig
import com.example.neugelb.apis.checkSuccessful
import com.example.neugelb.model.InfoAndCredits
import com.example.neugelb.model.MovieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(
    val app: Application,
) : AndroidViewModel(app) {

    var errorLiveData = mutableLiveDataOf("")
    var isLoadingMoviesLiveData = mutableLiveDataOf(true)
    var isLoadingMovieInfoLiveData = mutableLiveDataOf(false)
    var moviesLiveData = mutableLiveDataOf<List<MovieResult>?>(null)
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
            if (currentPage == 0) {
                isLoadingMoviesLiveData.postValue(true)
            }
            if (currentPage < totalNumberOfPages) {
                withContext(Dispatchers.IO) {
                    if (hasInternetConnection()) {
                        kotlin.runCatching {
                            ApiBuilders.getTMDBApi().getMovies(
                                BuildConfig.TMDB_API_KEY, currentPage + 1
                            ).execute().checkSuccessful()
                        }.onFailure {
                            errorLiveData.postValue(it.message)
                        }.onSuccess {
                            totalNumberOfPages = it.body()?.totalPages ?: 1
                            currentPage = it.body()?.page ?: 0
                            it.body()?.results?.let {
                                movies.addAll(it)
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
        val cm: ConnectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo;
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting;
        return isConnected;
    }

    suspend fun onMovieClicked(movieEntry: MovieResult) {
        if (hasInternetConnection()) {
            kotlin.runCatching {
                isLoadingMovieInfoLiveData.postValue(true)
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
}

class MoviesViewModelFactory(
    val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (MoviesViewModel(app) as T)
    }
}

fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> =
    MutableLiveData<T>().apply { this.value = value }
