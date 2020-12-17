package com.example.imdbviewer.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.imdbviewer.ui.mainscreen.GridLayout
import com.example.imdbviewer.ui.mainscreen.TmdbItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.imdbviewer.data.state.DataState
import com.example.imdbviewer.theme.GreenLight
import com.example.imdbviewer.ui.mainscreen.AppIconButton
import com.example.imdbviewer.util.ScreenNavigationEvents
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    screenNavigationEvents: (ScreenNavigationEvents) -> Unit
) {
    val viewState by viewModel.favoritesViewState.collectAsState()
    val syncState by viewModel.synStat.collectAsState()
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
            },
            actions = {
                SyncButton(
                    syncState = syncState,
                    onRefresh = viewModel::onRequestSync,
                )
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
fun SyncButton(
    syncState: DataState<Unit>?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
        when (syncState) {
            null -> AppIconButton(icon = Icons.Default.CloudCircle, onClick = onRefresh,modifier = modifier)
            is DataState.Loading -> AppIconButton(icon = Icons.Default.CloudDownload, onClick = {},modifier = modifier)
            is DataState.Success -> AppIconButton(icon = Icons.Default.CloudCircle,contentColor = GreenLight, onClick = onRefresh,modifier = modifier)
            is DataState.Failed -> AppIconButton(icon = Icons.Default.CloudCircle,contentColor = MaterialTheme.colors.error, onClick = onRefresh,modifier = modifier)
        }
}


@Composable
fun FavoriteMainContent(
    viewState: FavoriteScreenViewState,
    modifier: Modifier = Modifier,
    screenNavigationEvents: (ScreenNavigationEvents) -> Unit
) {
    val items = viewState.favorites

    GridLayout {
        for (item in items) {
            TmdbItem(
                item = item,
                onItemClick = {
                    screenNavigationEvents(ScreenNavigationEvents.NavigateToItemDetails(item = item))
                },
                modifier = Modifier.padding(8.dp).preferredHeight(250.dp)
            )
        }
    }
}