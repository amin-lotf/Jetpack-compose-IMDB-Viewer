package com.example.imdbviewer.data.network.api

import com.example.imdbviewer.BuildConfig
import com.example.imdbviewer.data.network.ImdbResponse
import com.example.imdbviewer.models.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbApi {
    @GET("{type}/${IMDB_CLIENT_ID}")
    suspend fun getMoviesByCategory(
        @Path("type") type:String
    ):ImdbResponse
    companion object{
        const val IMDB_CLIENT_ID=BuildConfig.IMDB_API_ACCESS_KEY
        const val RAPID_API_KEY=BuildConfig.RAPID_API_ACESS_KEY
        const val BASE_URL="https://imdb-api.com/en/API/"
    }
}