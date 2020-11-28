package com.example.imdbviewer.data.network.api

import com.example.imdbviewer.BuildConfig
import com.example.imdbviewer.data.network.ImdbResponse
import com.example.imdbviewer.data.network.RapidMovieResponse
import com.example.imdbviewer.data.network.RapidTVResponse
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidMovieDetails
import com.example.imdbviewer.models.RapidTVDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbApi {

    @GET("/")
    suspend fun getRapidMoviesByCategory(
        @Query("type") type:String,
        @Query("page") page:Int
    ):RapidMovieResponse

    @GET("/")
    suspend fun getRapidTVsByCategory(
        @Query("type") type:String,
        @Query("page") page:Int
    ):RapidTVResponse


    @GET("/")
    suspend fun getRapidMovieDetails(
        @Query("type") type:String,
        @Query("imdb") imdbId:String
    ):RapidMovieDetails

    @GET("/")
    suspend fun getRapidTVDetails(
        @Query("type") type:String,
        @Query("imdb") imdbId:String
    ):RapidTVDetails



    //TODO: remove if no use
    @GET("{type}/${IMDB_CLIENT_ID}")
    suspend fun getMoviesByCategory(
        @Path("type") type:String
    ):ImdbResponse


    companion object{
        const val IMDB_CLIENT_ID=BuildConfig.IMDB_API_ACCESS_KEY
        const val RAPID_API_KEY=BuildConfig.RAPID_API_ACESS_KEY
        const val BASE_URL="https://movies-tvshows-data-imdb.p.rapidapi.com"
        const val TV_DETAILS_TYPE="get-show-details"
        const val MOVIE_DETAILS_TYPE="get-movie-details"
    }
}