package com.example.imdbviewer.ui.favorites

import com.example.imdbviewer.models.tmdb.item.TmdbListItem

data class FavoriteScreenViewState(
    val favorites:List<TmdbListItem> = listOf()
)