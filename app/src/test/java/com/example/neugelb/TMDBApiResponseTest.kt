package com.example.neugelb

import com.example.neugelb.model.Cast
import com.example.neugelb.model.Credits
import com.example.neugelb.model.Crew
import com.example.neugelb.model.Genre
import com.example.neugelb.model.InfoAndCredits
import com.example.neugelb.model.MovieResult
import com.example.neugelb.model.MoviesResponse
import com.example.neugelb.model.ProductionCompany
import com.example.neugelb.model.ProductionCountry
import com.example.neugelb.model.Result
import com.example.neugelb.model.SpokenLanguage
import com.example.neugelb.model.Videos
import com.google.gson.GsonBuilder
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.io.FileReader

@RunWith(JUnit4::class)
class TMDBApiResponseTest {
    lateinit var moviesResponse: MoviesResponse
    lateinit var result1: MovieResult
    lateinit var result2: MovieResult
    lateinit var result3: MovieResult
    lateinit var movieInfoAndCredits: InfoAndCredits
    lateinit var videoResult1: Result
    lateinit var cast1: Cast
    lateinit var crew1: Crew
    lateinit var crew2: Crew
    lateinit var crew3: Crew

    @Before
    fun setup() {
        result1 = MovieResult(
            adult = false,
            backdropPath = "/gVBPRbiqczxj4igbR1p7f6ujRo.jpg",
            genreIds = listOf(35),
            id = 831227,
            originalLanguage = "en",
            originalTitle = "Liars and Cheats",
            overview = "Max Peters, known around the world, wakes to find his phone has been hacked. As the social media tornado begins, his publicist, acting coach, and manager assemble in his London flat to contain the situation and find out who is responsible.",
            popularity = 1.96,
            posterPath = "/bZLCk9KT8K7fJMTNLmGgqyT98xz.jpg",
            releaseDate = "2021-05-10",
            title = "Liars and Cheats",
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )
        result2 = MovieResult(
            adult = false,
            backdropPath = "/3y2XrlEehnxhHOc7hlF9ZwC48MF.jpg",
            genreIds = listOf(99, 10770, 36),
            id = 830921,
            originalLanguage = "fr",
            originalTitle = "10 mai 1981, le jour du grand soir",
            overview = "",
            popularity = 2.239,
            posterPath = "/lnDggyPba1Y2HpQD9gZWJgGaN4Y.jpg",
            releaseDate = "2021-05-10",
            title = "10 mai 1981, le jour du grand soir",
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )
        result3 = MovieResult(
            adult = false,
            backdropPath = null,
            genreIds = emptyList(),
            id = 830022,
            originalLanguage = "en",
            originalTitle = "A Biography of Great Proportions",
            overview = "A biography of Addison Perry-Franks and her struggles to fit in with society among the LGBT Community, and her struggles to be accepted, and her run for Texas House of Representatives in 2020.",
            popularity = 2.105,
            posterPath = "/txCxW8rAVzfB2gXH1mWAPB8Fo5Q.jpg",
            releaseDate = "2021-05-10",
            title = "A Biography of Great Proportions",
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )
        moviesResponse = MoviesResponse(
            page = 1,
            results = listOf(result1, result2, result3),
            totalPages = 2,
            totalResults = 3
        )
        videoResult1 = Result(
            id = "60a7b397142ef10057b46d57",
            iso_639_1 = "en",
            iso_3166_1 = "US",
            key = "EWcuGM8zjh0",
            name = "PINK All I Know So Far - Official Trailer | Prime Video",
            site = "YouTube",
            size = 1080,
            type = "Trailer"
        )
        cast1 = Cast(
            adult = false,
            gender = 1,
            id = 77271,
            known_for_department = "Acting",
            name = "Pink",
            original_name = "Pink",
            popularity = 1.4,
            profile_path = "/kRSHST66hj4B6KVuXBJzi6Q0jy3.jpg",
            cast_id = 2,
            character = "",
            credit_id = "60554ccb4083b3006bd14134",
            order = 1
        )
        crew1 = Crew(
            adult = false,
            gender = 2,
            id = 1276373,
            known_for_department = "Directing",
            name = "Michael Gracey",
            original_name = "Michael Gracey",
            popularity = 1.38,
            profile_path = "/aPfUH7DbWh7wxzylql5yD34S9g2.jpg",
            credit_id = "60554c4de16e5a003c6697fd",
            department = "Directing",
            job = "Director"
        )
        crew2 = Crew(
            adult = false,
            gender = 2,
            id = 1276373,
            known_for_department = "Directing",
            name = "Michael Gracey",
            original_name = "Michael Gracey",
            popularity = 1.38,
            profile_path = "/aPfUH7DbWh7wxzylql5yD34S9g2.jpg",
            credit_id = "60a7b50c142ef10040f11649",
            department = "Production",
            job = "Producer"
        )
        crew3 = Crew(
            adult = false,
            gender = 0,
            id = 1396671,
            known_for_department = "Directing",
            name = "David Spearing",
            original_name = "David Spearing",
            popularity = 0.6,
            profile_path = null,
            credit_id = "60a7b5807dfda6007af127bc",
            department = "Crew",
            job = "Cinematography"
        )
        movieInfoAndCredits = InfoAndCredits(
            adult = false,
            backdrop_path = "/1C7OJjGDnrFUGQIrXVy96Kbdp5h.jpg",
            belongs_to_collection = null,
            budget = 0,
            credits = Credits(listOf(cast1), listOf(crew1, crew2, crew3)),
            genres = listOf(
                Genre(99, "Documentary"),
                Genre(10402, "Music")
            ),
            homepage = "https://amazon.com/dp/B08ZJ5QLYZ",
            id = 807597,
            imdb_id = "tt14264844",
            original_language = "en",
            original_title = "P!nk: All I Know So Far",
            overview = "A behind-the-scenes look at P!NK as she balances family and life on the road, leading up to her first Wembley Stadium performance on 2019's \"Beautiful Trauma\" world tour.",
            popularity = 35.55,
            poster_path = "/3V5OGAgVuy8ugfJHwM5AlvFAky2.jpg",
            production_companies = listOf(
                ProductionCompany(
                    id = 154628,
                    logo_path = null,
                    name = "Lefty Paw Print",
                    origin_country = ""
                )
            ),
            production_countries = listOf(
                ProductionCountry(
                    "US",
                    "United States of America"
                )
            ),
            release_date = "2021-05-21",
            revenue = 0,
            runtime = 99,
            spoken_languages = listOf(
                SpokenLanguage(
                    english_name = "English",
                    iso_639_1 = "en",
                    name = "English"
                )
            ),
            status = "Released",
            tagline = "",
            title = "P!nk: All I Know So Far",
            video = false,
            vote_average = 9.0,
            vote_count = 2,
            videos = Videos(listOf(videoResult1))
        )
    }

    @Test
    fun getMoviesResponseTest() {
        val savedMoviesResponse = GsonBuilder().create().fromJson(
            FileReader("./MoviesResponseJson"),
            MoviesResponse::class.java
        )
        assertEquals(moviesResponse.results, savedMoviesResponse.results)
        assertEquals(moviesResponse.page, savedMoviesResponse.page)
        assertEquals(moviesResponse.totalPages, savedMoviesResponse.totalPages)
        assertEquals(moviesResponse.totalResults, savedMoviesResponse.totalResults)
    }

    @Test
    fun getMovieInfoResponseTest() {
        val savedMovieInfo = GsonBuilder().create().fromJson(
            FileReader("./MovieInfoResponseJson"),
            InfoAndCredits::class.java
        )
        assertEquals(movieInfoAndCredits.adult, savedMovieInfo.adult)
        assertEquals(movieInfoAndCredits.backdrop_path, savedMovieInfo.backdrop_path)
        assertEquals(movieInfoAndCredits.belongs_to_collection, savedMovieInfo.belongs_to_collection)
        assertEquals(movieInfoAndCredits.budget, savedMovieInfo.budget)
        assertEquals(movieInfoAndCredits.credits, savedMovieInfo.credits)
        assertEquals(movieInfoAndCredits.genres, savedMovieInfo.genres)
        assertEquals(movieInfoAndCredits.homepage, savedMovieInfo.homepage)
        assertEquals(movieInfoAndCredits.id, savedMovieInfo.id)
        assertEquals(movieInfoAndCredits.imdb_id, savedMovieInfo.imdb_id)
        assertEquals(movieInfoAndCredits.original_language, savedMovieInfo.original_language)
        assertEquals(movieInfoAndCredits.original_title, savedMovieInfo.original_title)
        assertEquals(movieInfoAndCredits.overview, savedMovieInfo.overview)
        assertEquals(movieInfoAndCredits.popularity, savedMovieInfo.popularity, 0.0)
        assertEquals(movieInfoAndCredits.poster_path, savedMovieInfo.poster_path)
        assertEquals(movieInfoAndCredits.production_companies, savedMovieInfo.production_companies)
        assertEquals(movieInfoAndCredits.production_countries, savedMovieInfo.production_countries)
        assertEquals(movieInfoAndCredits.release_date, savedMovieInfo.release_date)
        assertEquals(movieInfoAndCredits.revenue, savedMovieInfo.revenue)
        assertEquals(movieInfoAndCredits.runtime, savedMovieInfo.runtime)
        assertEquals(movieInfoAndCredits.videos, savedMovieInfo.videos)
        assertEquals(movieInfoAndCredits.video, savedMovieInfo.video)
    }
}
