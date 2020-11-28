package com.example.imdbviewer.ui.mainscreen

import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidMovie
import com.example.imdbviewer.models.RapidTV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MainScreenViewState(
     val moviesViewState: CategoryViewState<RapidMovie> =CategoryViewState(),
     val tvsViewState: CategoryViewState<RapidTV> =CategoryViewState(),
) {
}