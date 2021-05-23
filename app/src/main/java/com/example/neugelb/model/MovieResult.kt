package com.example.neugelb.model

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName

data class MovieResult(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
) : AutoCompleteItem {
    override fun filter(query: String): Boolean {
        return title.toLowerCase().startsWith(query.toLowerCase())
    }
}

@Stable
interface AutoCompleteItem {
    fun filter(query: String): Boolean
}