package com.example.imdbviewer.util

import com.example.imdbviewer.domain_models.TmdbListItem

sealed class ScreenNavigationEvents {
    data class NavigateToItemDetails(val item: TmdbListItem): ScreenNavigationEvents()
    object NavigateToFavorites: ScreenNavigationEvents()
    object NavigateBack: ScreenNavigationEvents()
    object NavigateToSignInActivity: ScreenNavigationEvents()
    object NavigateToChoosePhotoActivity:ScreenNavigationEvents()

}