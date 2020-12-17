package com.example.imdbviewer.data.network.tmdb.api

import com.example.imdbviewer.data.network.tmdb.models.item.tv.TmdbTVListItemDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmdbTVListResponse(
    @Expose
    @SerializedName("results") override val results : List<TmdbTVListItemDto>,
    @Expose
    @SerializedName("total_results")override val totalResults : Int,
    @Expose
    @SerializedName("page")override val page : Int,
    @Expose
    @SerializedName("total_pages")override val totalPages : Int
):TmdbListResponse<TmdbTVListItemDto>(results,totalPages,page,totalPages)