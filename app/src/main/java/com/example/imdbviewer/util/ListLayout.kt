package com.example.imdbviewer.util

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@Composable
fun <T : Any> RowLayoutPagination(
    flow: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    handleLoadStates: Boolean = true,
    checkIfEmptyList: (Boolean) -> Unit,
    content: @Composable (T) -> Unit
) {
    val items = flow.collectAsLazyPagingItems()
    LazyRow(
        modifier = if (handleLoadStates) {
            modifier.preferredHeight(250.dp)
        } else modifier
    ) {
        items(items) { item ->
            item?.let {
                Box(modifier = Modifier.preferredWidth(150.dp)) {
                    content(item)
                }
            }
        }
        if (handleLoadStates) {
            manageLoadState(loadState = items.loadState) {
                items.retry()
            }
        }
        if (items.loadState.refresh is LoadState.NotLoading) {
            checkIfEmptyList(items.itemCount == 0)
        }
    }
}


@Composable
fun <T : Any> RowLayout(
    items: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    LazyRow(modifier = modifier) {
        items(items) { item ->
            Box(modifier = Modifier) {
                content(item)
            }
        }
    }
}

fun LazyListScope.manageLoadState(
    loadState: CombinedLoadStates,
    onRetry: () -> Unit
) {
    when {
        loadState.refresh is LoadState.Loading -> {
            item {
                Box(modifier = Modifier.fillParentMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }

        loadState.append is LoadState.Loading -> {
            item {
                Box(modifier = Modifier.fillParentMaxHeight()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }

        loadState.append is LoadState.Error -> {
            item {
                Box(modifier = Modifier.fillParentMaxHeight()) {
                    Icon(Icons.Default.Refresh.copy(
                        defaultWidth = 48.dp,
                        defaultHeight = 48.dp
                    ),
                        modifier = Modifier.align(Alignment.Center)
                            .clickable(onClick = { onRetry() }
                            ))
                }
            }
        }

        loadState.refresh is LoadState.Error -> {
            item {
                Box(modifier = Modifier.fillParentMaxSize()) {
                    Icon(
                        Icons.Filled.Refresh.copy(
                            defaultWidth = 48.dp,
                            defaultHeight = 48.dp
                        ),
                        modifier = Modifier.align(Alignment.Center)
                            .clickable(onClick = { onRetry() })
                    )
                }
            }
        }
    }
}