package com.example.imdbviewer.ui.favorites

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.imdbviewer.ui.mainscreen.GridLayout
import com.example.imdbviewer.ui.mainscreen.TmdbItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imdbviewer.ui.mainscreen.BodyContent
import com.example.imdbviewer.ui.mainscreen.ScreenNavigationEvents

@ExperimentalCoroutinesApi
@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    screenNavigationEvents: (ScreenNavigationEvents) -> Unit
) {
    val viewState by viewModel.favoritesViewState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Favorites") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        screenNavigationEvents(ScreenNavigationEvents.NavigateBack)
                    }) {
                    Icon(Icons.Default.ArrowBack)
                }
            }
        )
    }) {
        FavoriteMainContent(
            viewState = viewState,
            screenNavigationEvents = screenNavigationEvents
        )
    }


}


@Composable
fun FavoriteMainContent(
    viewState: FavoriteScreenViewState,
    modifier: Modifier = Modifier,
    screenNavigationEvents: (ScreenNavigationEvents) -> Unit) {
    val items = viewState.favorites

    GridLayout {
        for (item in items) {
            TmdbItem(
                item = item,
                onItemClick = {
                    screenNavigationEvents(ScreenNavigationEvents.NavigateToItemDetails(item = item))},
                modifier = Modifier.padding(8.dp).preferredHeight(250.dp)
            )
        }
    }
}