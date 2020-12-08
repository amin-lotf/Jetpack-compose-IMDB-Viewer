package com.example.imdbviewer.data.network.tmdb.api

import com.example.imdbviewer.models.tmdb.item.tv.TmdbTVListItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmdbTVListResponse(
    @Expose
    @SerializedName("results") override val results : List<TmdbTVListItem>,
    @Expose
    @SerializedName("total_results")override val totalResults : Int,
    @Expose
    @SerializedName("page")override val page : Int,
    @Expose
    @SerializedName("total_pages")override val totalPages : Int
):TmdbListResponse<TmdbTVListItem>(results,totalPages,page,totalPages)