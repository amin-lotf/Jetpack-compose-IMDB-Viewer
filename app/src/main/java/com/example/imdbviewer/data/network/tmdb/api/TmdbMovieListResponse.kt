package com.example.imdbviewer.data.network.tmdb.api

import com.example.imdbviewer.models.tmdb.item.movie.TmdbMovieListItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmdbMovieListResponse(
    @Expose
    @SerializedName("results") override val results : List<TmdbMovieListItem>,
    @Expose
    @SerializedName("total_results")override val totalResults : Int,
    @Expose
    @SerializedName("page")override val page : Int,
    @Expose
    @SerializedName("total_pages")override val totalPages : Int
):TmdbListResponse<TmdbMovieListItem>(results,totalPages,page,totalPages)