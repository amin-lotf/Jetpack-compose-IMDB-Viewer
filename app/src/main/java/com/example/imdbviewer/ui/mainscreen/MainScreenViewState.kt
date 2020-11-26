package com.example.imdbviewer.ui.mainscreen

import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MainScreenViewState(
     val moviesViewState: CategoryViewState=CategoryViewState(),
     val tvsViewState: CategoryViewState=CategoryViewState(),
     val boxOfficeViewState: CategoryViewState=CategoryViewState()

) {
}