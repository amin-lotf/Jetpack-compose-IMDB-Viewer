package com.example.imdbviewer.data.network.rapidapi

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.network.rapidapi.api.RapidResponse
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidItem
import com.example.imdbviewer.models.RapidMovie
import com.example.imdbviewer.models.RapidTV
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class RapidMovieResponse(

    @Expose
    @SerializedName("movie_results")
    override val items: List<RapidMovie>?,

    @Expose
    override val status:String?,

    @Expose
    @SerializedName("status_message")
    override val statusMessage: String?,

    @Expose
    override val results:Int?,

    @Expose
    @SerializedName("Total_results")
    override val totalItems: Int?
): RapidResponse(items,status,statusMessage,results,totalItems)