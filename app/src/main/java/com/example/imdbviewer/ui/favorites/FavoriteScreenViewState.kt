package com.example.imdbviewer.ui.favorites

import com.example.imdbviewer.domain_models.TmdbListItem

data class FavoriteScreenViewState(
    val favorites: List<TmdbListItem> = listOf()
)