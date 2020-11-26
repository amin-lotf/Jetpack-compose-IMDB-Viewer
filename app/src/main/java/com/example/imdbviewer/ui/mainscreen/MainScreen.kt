package com.example.imdbviewer.ui.mainscreen

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.theme.keyline1

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import dev.chrisbanes.accompanist.coil.CoilImage

val TAG = "aminjoon"

@ExperimentalCoroutinesApi

@Composable
fun MainScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val viewState by viewModel.mainScreenState.collectAsState(initial = MainScreenViewState())

    ScrollableColumn(modifier = modifier,contentPadding = PaddingValues(top = 16.dp,start = 4.dp,end = 4.dp)) {
        CategorySection(
            viewState = viewState.moviesViewState,
            type = CategoryType.Movies,
            onRefresh = {},
            onAppend = {},
            onCategorySelected = viewModel::changeCategory
        )
        Spacer(Modifier.preferredHeight(8.dp))
        CategorySection(
            viewState = viewState.tvsViewState,
            type = CategoryType.TVs,
            onRefresh = {},
            onAppend = {},
            onCategorySelected = viewModel::changeCategory
        )
        Spacer(Modifier.preferredHeight(8.dp))
        CategorySection(
            viewState = viewState.boxOfficeViewState,
            type = CategoryType.BoxOffice,
            onRefresh = {},
            onAppend = {},
            onCategorySelected = viewModel::changeCategory
        )

    }
}


@Composable
fun CategorySection(
    viewState: CategoryViewState,
    type: CategoryType,
    modifier: Modifier = Modifier,
    onRefresh: (CategoryType) -> Unit,
    onAppend: (CategoryType) -> Unit,
    onCategorySelected: (Category) -> Unit,
) {
    Text(text = type.title)
    Divider(
        color = MaterialTheme.colors.onSurface,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
    )
    val selectedMovieCategory = viewState.selectedCategory
    if (selectedMovieCategory != null && viewState.subCategories.isNotEmpty()) {
        CategoryTabs(
            categories = viewState.subCategories,
            selectedCategory = selectedMovieCategory,
            onCategorySelected = {onCategorySelected(it)})
    }

    RowLayout(
        flow = viewState.pagingData,
        section = CategoryType.Movies,
        onRefresh = {onRefresh(it)},
        onAppend = {onAppend(it)},
        //modifier = Modifier.fillMaxHeight(1f/2)
    ) { item ->
        MovieItem(movie = item, modifier = Modifier.padding(4.dp).fillMaxSize())

    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}

@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
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
    section: CategoryType,
    onRefresh: (CategoryType) -> Unit,
    onAppend: (CategoryType) -> Unit,
    content: @Composable BoxScope.(T) -> Unit
) {
    val items = flow.collectAsLazyPagingItems()

    if (items.loadState.append.endOfPaginationReached) {
        onAppend(section)
    }
    if (items.loadState.refresh == LoadState.Loading) {

        // val first=items[0]
        onRefresh(section)
    }
    LazyRow(modifier = modifier.preferredHeight(250.dp)) {
        items(items) { item ->
            item?.let {
                Box(modifier = Modifier.fillParentMaxWidth(1f / 3)) {
                    content(item)
                }
            }
        }
    }


}


@Composable
fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.clip(shape = RoundedCornerShape(8.dp))) {
        Column {
            CoilImage(
                data = movie.thumbnail,
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

