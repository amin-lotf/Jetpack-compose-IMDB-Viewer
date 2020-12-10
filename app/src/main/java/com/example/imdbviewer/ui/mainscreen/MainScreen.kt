package com.example.imdbviewer.ui.mainscreen


import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData


import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.models.tmdb.item.TmdbListItem
import com.example.imdbviewer.theme.keyline1
import com.example.imdbviewer.util.ImageConfig
import com.example.imdbviewer.util.ItemSwitcher
import com.example.imdbviewer.util.ItemTransitionState
import com.example.imdbviewer.util.RowLayoutPagination

import kotlinx.coroutines.ExperimentalCoroutinesApi
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

private val TAG = "aminjoon"


@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalFocus
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    screenNavigationEvents: (ScreenNavigationEvents) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewState by viewModel.mainScreenState.collectAsState()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {

            DrawerContent(
                navigationEvent = screenNavigationEvents,
                scaffoldState=scaffoldState
            )
                        },
        topBar = {
            TopAppBar(
                title = {
                    if (!viewState.inSearchMode) {
                        Text(text = "Movie Info")
                    } else {
                        SearchInputText(
                            text = viewState.searchQuery,
                            onTextChanged = viewModel::performSearch
                        )
                    }
                },
                elevation = 8.dp,
                navigationIcon =
                if (!viewState.inSearchMode) {
                    {
                        IconButton(onClick = {scaffoldState.drawerState.open()}) {
                            Icon(Icons.Default.Menu)
                        }
                    }
                } else {
                    null
                },
                actions = {
                    SearchButton(
                        onClick = viewModel::switchToSearchMode,
                        inSearchMode = viewState.inSearchMode
                    )
                }
            )
        },
        bodyContent = {
            MainContent(
                viewState = viewState,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                navigationEvents = screenNavigationEvents,
                onCategorySelected = viewModel::changeCategory
            )
        }
    )
}

@Composable
fun MainContent(
    viewState: MainScreenViewState,
    navigationEvents: (ScreenNavigationEvents) -> Unit,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableColumn(modifier = modifier) {
        CategorySection(
            viewState = viewState.moviesViewState,
            type = CategoryType.Movies,
            inSearchMode = viewState.inSearchMode,
            mainScreenInteractionEvents = navigationEvents,
            onCategorySelected = onCategorySelected
        )
        Spacer(Modifier.preferredHeight(8.dp))
        CategorySection(
            viewState = viewState.tvsViewState,
            type = CategoryType.TVs,
            inSearchMode = viewState.inSearchMode,
            mainScreenInteractionEvents = navigationEvents,
            onCategorySelected = onCategorySelected
        )
        Spacer(Modifier.preferredHeight(8.dp))
    }
}


@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    navigationEvent: (ScreenNavigationEvents) -> Unit,
    scaffoldState: ScaffoldState
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {

        Button(
            onClick = {
                scaffoldState.drawerState.close(onClosed = {
                                    navigationEvent(
                    ScreenNavigationEvents.NavigateToFavorites
                )
                })
            },
            elevation = null,
            colors = ButtonConstants.defaultButtonColors(
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),

            ) {
            Icon(Icons.Default.Favorite, modifier = Modifier.preferredSize(20.dp))
            Spacer(modifier = Modifier.preferredWidth(8.dp))
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }


    }
}


@Composable
fun CategorySection(
    viewState: CategoryViewState,
    type: CategoryType,
    inSearchMode: Boolean,
    modifier: Modifier = Modifier,
    mainScreenInteractionEvents: (ScreenNavigationEvents) -> Unit,
    onCategorySelected: (Category) -> Unit,
) {
    val (isEmpty, setCheck) = remember { mutableStateOf(true) }
    Column(modifier = modifier) {

        if (!inSearchMode || !isEmpty) {
            Text(text = type.title)
            Divider(
                color = MaterialTheme.colors.onSurface,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
        val selectedCategory = viewState.selectedCategory

        if (selectedCategory != null) {
            if (inSearchMode) {
                TmdbRowPagination(
                    flow = viewState.pagingData,
                    handleLoadState = false,
                    checkIfEmptyList = setCheck,
                    onItemClick = { tmdbItemList ->
                        mainScreenInteractionEvents(
                            ScreenNavigationEvents.NavigateToItemDetails(
                                item = tmdbItemList
                            )
                        )
                    }
                )
            } else {
                TmdbSectionWithTab(
                    subCategories = viewState.subCategories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected
                ) { transitionState ->
                    TmdbRowPagination(
                        flow = viewState.pagingData,
                        checkIfEmptyList = setCheck,
                        modifier = Modifier.graphicsLayer(
                            translationX = transitionState[Offset],
                            alpha = transitionState[Alpha]
                        ),
                        onItemClick = { tmdbItemList ->
                            mainScreenInteractionEvents(
                                ScreenNavigationEvents.NavigateToItemDetails(
                                    item = tmdbItemList
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TmdbSectionWithTab(
    subCategories: List<Category>,
    selectedCategory: Category,
    modifier: Modifier = Modifier,
    onCategorySelected: (Category) -> Unit,
    content: @Composable (TransitionState) -> Unit
) {

    Column(modifier = modifier) {
        var prevSelectedCategory by remember { mutableStateOf<Category?>(null) }
        CategoryTabs(
            categories = subCategories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        val reverseTransition = prevSelectedCategory?.let { p ->
            subCategories.indexOf(p) > subCategories.indexOf(
                selectedCategory
            )
        } ?: false

        val transitionOffset = with(AmbientDensity.current) { 16.dp.toPx() }
        ItemSwitcher(
            current = selectedCategory,
            transitionDefinition = getChoiceChipTransitionDefinition(
                reverse = reverseTransition,
                offsetPx = transitionOffset
            )
        ) { _, transitionState ->
            content(transitionState)
        }
        onCommit(selectedCategory) {
            prevSelectedCategory = selectedCategory
        }
    }
}


@Composable
fun TmdbRowPagination(
    flow: Flow<PagingData<TmdbListItem>>,
    modifier: Modifier = Modifier,
    handleLoadState: Boolean = true,
    checkIfEmptyList: (Boolean) -> Unit,
    onItemClick: (TmdbListItem) -> Unit
) = RowLayoutPagination(
    flow = flow,
    handleLoadStates = handleLoadState,
    checkIfEmptyList = checkIfEmptyList,
    modifier = modifier
) { item ->
    TmdbItem(
        item = item,
        modifier = Modifier.padding(4.dp).preferredHeight(250.dp),
        onItemClick = onItemClick
    )
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
        modifier = modifier.fillMaxWidth(),
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
fun TmdbItem(
    item: TmdbListItem,
    modifier: Modifier = Modifier,
    onItemClick: (TmdbListItem) -> Unit,
) {


    Card(
        modifier = modifier.clip(shape = RoundedCornerShape(8.dp))
            .clickable(
                onClick = {

                    onItemClick(item)
                }
            )
    ) {
        Column(modifier = Modifier) {
            val imageUrl = if (item.posterPath.isNotBlank()) {
                ImageConfig.profilePath + item.posterPath
            } else {
                //just a placeholder for demo
                "https://critics.io/img/movies/poster-placeholder.png"
            }
            CoilImage(
                data = imageUrl,
                fadeIn = true,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.weight(.75f).fillMaxWidth()
            )
            Spacer(modifier = Modifier.preferredHeight(4.dp))
            Box(
                modifier = Modifier.weight(0.25f).padding(4.dp).fillMaxWidth()
            ) {
                val padding = Modifier.padding(horizontal = 8.dp)
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = padding.align(Alignment.TopStart),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,

                    )
                Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = item.year.toString(),
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


