package com.example.imdbviewer.data.network.rapidapi.api

import com.example.imdbviewer.data.network.rapidapi.RapidMovieResponse
import com.example.imdbviewer.data.network.rapidapi.RapidTVResponse
import com.example.imdbviewer.models.RapidMovieDetails
import com.example.imdbviewer.models.RapidTVDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RapidApi {

    @GET("/")
    suspend fun getRapidMoviesByCategory(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<RapidMovieResponse>

    @GET("/")
    suspend fun getRapidTVsByCategory(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<RapidTVResponse>


    @GET("/")
    suspend fun getRapidMovieDetails(
        @Query("type") type: String,
        @Query("imdb") imdbId: String
    ): RapidMovieDetails

    @GET("/")
    suspend fun getRapidTVDetails(
        @Query("type") type: String,
        @Query("imdb") imdbId: String
    ): RapidTVDetails



    companion object {

        const val BASE_URL = "https://movies-tvshows-data-imdb.p.rapidapi.com"
        const val TV_DETAILS_TYPE = "get-show-details"
        const val MOVIE_DETAILS_TYPE = "get-movie-details"
    }
}