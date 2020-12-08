package com.example.imdbviewer.ui.detailscreen

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.models.tmdb.item.TmdbItemDetails
import com.example.imdbviewer.models.tmdb.item.movie.TmdbMovieDetails
import com.example.imdbviewer.models.tmdb.item.tv.TmdbTVDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class DetailScreenViewState(
    val itemDetails: TmdbItemDetails?=null,

)