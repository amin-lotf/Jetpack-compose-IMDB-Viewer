package com.example.imdbviewer.data.network.tmdb.api

import com.example.imdbviewer.data.network.tmdb.models.item.movie.TmdbMovieDetailsDto
import com.example.imdbviewer.data.network.tmdb.models.item.tv.TmdbTVDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("search/movie")
    suspend fun queryMovies(
        @Query("query") query:String,
        @Query("api_key") apiKey:String,
        @Query("page") page:Int
    ):Response<TmdbMovieListResponse>

    @GET("search/tv")
    suspend fun queryTVs(
        @Query("query") query:String,
        @Query("api_key") apiKey:String,
        @Query("page") page:Int
    ):Response<TmdbTVListResponse>


    @GET("{type}/{category}")
    suspend fun  getMoviesByCategory(
        @Path("type") type:String,
        @Path("category") category:String,
        @Query("api_key") apiKey:String,
        @Query("page") page:Int
    ):Response<TmdbMovieListResponse>

    @GET("{type}/{category}")
    suspend fun  getTvsByCategory(
        @Path("type") type:String,
        @Path("category") category:String,
        @Query("api_key") apiKey:String,
        @Query("page") page:Int
    ):Response<TmdbTVListResponse>


    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId:Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") details:String="credits"
    ):Response<TmdbMovieDetailsDto>

    @GET("tv/{tvId}")
    suspend fun getTVDetails(
        @Path("tvId") tvId:Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") details:String="credits"
    ):Response<TmdbTVDetailsDto>

    companion object{
        const val BASE_URL="https://api.themoviedb.org/3/"
    }
}