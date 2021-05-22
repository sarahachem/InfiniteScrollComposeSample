package com.example.neugelb.ui

import android.app.Application
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(
    app: Application,
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
        viewModelScope.launch {
            if (currentPage == 0) {
                isLoadingMoviesLiveData.postValue(true)
            }
            if (currentPage < totalNumberOfPages) {
                withContext(Dispatchers.IO) {
                    kotlin.runCatching {
                        ApiBuilders.getTMDBApi().getMovies(
                            BuildConfig.TMDB_API_KEY, currentPage + 1
                        ).execute().checkSuccessful()
                    }.onFailure {
                        errorLiveData.postValue(it.message)
                    }.onSuccess {
                        totalNumberOfPages = it.body()?.totalPages ?: 0
                        currentPage = it.body()?.page ?: 0
                        it.body()?.results?.let {
                            movies.addAll(it)
                            moviesLiveData.postValue(movies)
                        }
                    }
                }
            }
            if (currentPage == 1) {
                isLoadingMoviesLiveData.postValue(false)
            }
        }
    }

    suspend fun onMovieClicked(movieEntry: MovieResult) {
        kotlin.runCatching {
            setLoading(true)
            getMovieInfoAndCredits(movieEntry)
        }.onSuccess {
            setLoading(false)
        }.onFailure {
            setLoading(false)
            errorLiveData.postValue(it.message)
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

    private fun setLoading(loading: Boolean) {
        isLoadingMovieInfoLiveData.postValue(loading)
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
