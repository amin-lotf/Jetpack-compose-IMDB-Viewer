package com.example.imdbviewer.ui.mainscreen

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.models.tmdb.item.TmdbListItem

sealed class MainScreenInteractionEvents {
    data class OpenItemDetails(val item: TmdbListItem, val type:CategoryType):MainScreenInteractionEvents()
    data class AddToWatchList(val item: TmdbListItem):MainScreenInteractionEvents()
    data class RemoveFromWatchList(val item: TmdbListItem):MainScreenInteractionEvents()

}