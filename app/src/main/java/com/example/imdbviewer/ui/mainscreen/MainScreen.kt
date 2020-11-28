package com.example.imdbviewer.ui.mainscreen

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items


import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.NewCategory
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidItem
import com.example.imdbviewer.theme.keyline1
import com.example.imdbviewer.util.ItemSwitcher
import com.example.imdbviewer.util.ItemTransitionState

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage

val TAG = "aminjoon"

@ExperimentalCoroutinesApi

@Composable
fun MainScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val viewState by viewModel.mainScreenState.collectAsState(initial = MainScreenViewState())

    ScrollableColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 16.dp, start = 4.dp, end = 4.dp)
    ) {
        CategorySection(
            viewState = viewState.moviesViewState,
            type = CategoryType.Movies,
            onCategorySelected = viewModel::changeCategory
        )
        Spacer(Modifier.preferredHeight(8.dp))
        CategorySection(
            viewState = viewState.tvsViewState,
            type = CategoryType.TVs,
            onCategorySelected = viewModel::changeCategory
        )
        Spacer(Modifier.preferredHeight(8.dp))

    }
}


@Composable
fun <T : RapidItem> CategorySection(
    viewState: CategoryViewState<T>,
    type: CategoryType,
    onCategorySelected: (NewCategory) -> Unit,
) {
    Text(text = type.title)
    Divider(
        color = MaterialTheme.colors.onSurface,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
    )
    val selectedMovieCategory = viewState.selectedCategory
    if (selectedMovieCategory != null && viewState.subCategories.isNotEmpty()) {

        var prevSelectedCategory by remember { mutableStateOf<NewCategory?>(null) }

        CategoryTabs(
            categories = viewState.subCategories,
            selectedCategory = selectedMovieCategory,
            onCategorySelected = { onCategorySelected(it) })


        var reverseTransition = prevSelectedCategory?.let { p ->
            viewState.subCategories.indexOf(p) > viewState.subCategories.indexOf(
                selectedMovieCategory
            )
        } ?: false

        val transitionOffset = with(DensityAmbient.current) { 16.dp.toPx() }

        ItemSwitcher(
            current = selectedMovieCategory,
            transitionDefinition = getChoiceChipTransitionDefinition(
                reverse = reverseTransition,
                offsetPx = transitionOffset
            )
        ) {category, transitionState ->
            RowLayout(
                flow = viewState.pagingData,
                modifier = Modifier.drawLayer(
                    translationX = transitionState[Offset],
                    alpha = transitionState[Alpha]
                )
            ) { item ->
                MovieItem(movie = item, modifier = Modifier.padding(4.dp).fillMaxSize())
            }
        }

        onCommit(selectedMovieCategory){
            prevSelectedCategory=selectedMovieCategory
        }

    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}

@Composable
fun CategoryTabs(
    categories: List<NewCategory>,
    selectedCategory: NewCategory,
    onCategorySelected: (NewCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTabIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        divider = emptyContent(),
        edgePadding = keyline1,
        indicator = emptyTabIndicator,
        modifier = modifier.fillMaxWidth()
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onCategorySelected(category) }
            ) {
                CategoryItem(
                    label = category.label,
                    selected = index == selectedTabIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Composable
fun <T : Any> RowLayout(
    flow: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(T) -> Unit
) {
    val items = flow.collectAsLazyPagingItems()

        LazyRow(
            modifier = modifier.preferredHeight(250.dp)
        ) {
            items(items) { item ->
                item?.let {
                    Box(modifier = Modifier.fillParentMaxWidth(1f / 3)) {
                        content(item)
                    }
                }
            }
            manageLoadState(loadState = items.loadState) {
                items.retry()
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
                    Icon(
                        asset = Icons.Default.Refresh.copy(
                            defaultWidth = 48.dp,
                            defaultHeight = 48.dp
                        ),
                        modifier = Modifier.align(Alignment.Center)
                            .clickable(onClick = { onRetry() }

                            )
                    )
                }
            }
        }

        loadState.refresh is LoadState.Error -> {
            item {
                Box(modifier = Modifier.fillParentMaxSize()) {
                    Icon(
                        asset = Icons.Filled.Refresh.copy(
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


@Composable
fun MovieItem(
    movie: RapidItem,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.clip(shape = RoundedCornerShape(8.dp))) {
        Column {
            CoilImage(
                data = "https://image.tmdb.org/t/p/original/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                fadeIn = true,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.weight(.75f)
            )
            Spacer(modifier = Modifier.preferredHeight(4.dp))
            Box(
                modifier = Modifier.weight(0.25f).padding(4.dp)
            ) {
                val padding = Modifier.padding(horizontal = 8.dp)
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = padding.align(Alignment.TopStart),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,

                    )
                Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = movie.year.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = padding.align(Alignment.BottomStart),
                    )
                }
            }
        }
    }
}

private val Alpha = FloatPropKey("alpha")
private val Offset = FloatPropKey("offset")

@Composable
private fun getChoiceChipTransitionDefinition(
    duration: Int = 183,
    offsetPx: Float,
    reverse: Boolean = false
): TransitionDefinition<ItemTransitionState> = remember(reverse, offsetPx, duration) {
    transitionDefinition {
        state(ItemTransitionState.Visible) {
            this[Alpha] = 1f
            this[Offset] = 0f
        }
        state(ItemTransitionState.BecomingVisible) {
            this[Alpha] = 0f
            this[Offset] = if (reverse) -offsetPx else offsetPx
        }
        state(ItemTransitionState.BecomingNotVisible) {
            this[Alpha] = 0f
            this[Offset] = if (reverse) offsetPx else -offsetPx
        }

        val halfDuration = duration

        transition(
            fromState = ItemTransitionState.BecomingVisible,
            toState = ItemTransitionState.Visible
        ) {
            // TODO: look at whether this can be implemented using `spring` to enable
            //  interruptions, etc
            Alpha using tween(
                durationMillis = halfDuration,
                delayMillis = halfDuration,
                easing = LinearEasing
            )
            Offset using tween(
                durationMillis = halfDuration,
                delayMillis = halfDuration,
                easing = LinearOutSlowInEasing
            )
        }

        transition(
            fromState = ItemTransitionState.Visible,
            toState = ItemTransitionState.BecomingNotVisible
        ) {
            Alpha using tween(
                durationMillis = 1,
                easing = LinearEasing,
                delayMillis = 1
            )
            Offset using tween(
                durationMillis = 1,
                easing = LinearOutSlowInEasing,
                delayMillis = 1
            )
        }
    }
}

private const val DelayForContentToLoad = 20

