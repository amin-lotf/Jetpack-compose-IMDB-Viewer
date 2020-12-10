package com.example.imdbviewer.ui.mainscreen

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.models.tmdb.item.TmdbListItem

sealed class ScreenNavigationEvents {
    data class NavigateToItemDetails(val item: TmdbListItem):ScreenNavigationEvents()
    object NavigateToFavorites:ScreenNavigationEvents()
    object NavigateBack:ScreenNavigationEvents()
    object NavigateToSignInActivity:ScreenNavigationEvents()

}