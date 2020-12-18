package com.example.imdbviewer.data.network.tmdb.api

import com.example.imdbviewer.data.network.tmdb.models.item.movie.TmdbMovieListItemDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmdbMovieListResponse(
    @Expose
    @SerializedName("results") override val results: List<TmdbMovieListItemDto>,
    @Expose
    @SerializedName("total_results") override val totalResults: Int,
    @Expose
    @SerializedName("page") override val page: Int,
    @Expose
    @SerializedName("total_pages") override val totalPages: Int
) : TmdbListResponse<TmdbMovieListItemDto>(results, totalPages, page, totalPages)