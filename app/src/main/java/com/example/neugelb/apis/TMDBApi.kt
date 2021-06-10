package com.example.neugelb.apis

import com.example.neugelb.model.InfoAndCredits
import com.example.neugelb.model.MoviesResponse
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val TMDB_URL_API = "https://api.themoviedb.org"
const val POSTER_PATH_URL_API = "https://www.themoviedb.org/t/p/w780/"

interface TMDBApi {

    //TODO use retrofit coroutines
    @GET("/3/discover/movie?sort_by=primary_release_date.desc&include_adult=false&include_video=false&with_watch_monetization_types=flatrate")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("primary_release_date.lte") releaseDate: String = LocalDate.now().toString(
            DateTimeFormat.forPattern("YYYY-MM-dd")
        )
    ): Call<MoviesResponse>

    //TODO use retrofit coroutines
    @GET("/3/movie/{id}")
    fun getMovieInfoAndCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") extraQuery: String = "credits,videos"
    ): Call<InfoAndCredits>
}
