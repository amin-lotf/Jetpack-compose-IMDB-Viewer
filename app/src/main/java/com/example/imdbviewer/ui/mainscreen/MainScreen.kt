package com.example.imdbviewer.ui.mainscreen


import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.PagingData


import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.data.state.DataState
import com.example.imdbviewer.domain_models.User
import com.example.imdbviewer.domain_models.TmdbListItem
import com.example.imdbviewer.theme.keyline1
import com.example.imdbviewer.util.*

import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KFunction1

private val TAG = "aminjoon"


@FlowPreview
@ExperimentalCoroutinesApi

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    inDarkMode: Boolean,
    screenNavigationEvents: (ScreenNavigationEvents) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewState by viewModel.mainScreenState.collectAsState()
    val userStatus by viewModel.isUserSignedIn.collectAsState()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.primary,
        drawerContent = {

            DrawerContent(
                isUserSignedIn = userStatus,
                userInEdit = viewModel.userInEdit,
                navigationEvent = screenNavigationEvents,
                scaffoldState = scaffoldState,
                userInfoFlow = viewModel.getUserInfo(),
                onSignOut = viewModel::signOutUser,
                onEditUserInfo = viewModel::onEditUserInfo,
                onEditDone = viewModel::onEditUserDone
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    if (!viewState.inSearchMode) {
                        Text(text = "Movie Info")
                    } else {
                        MainInputText(
                            text = viewState.searchQuery,
                            onTextChanged = viewModel::performSearch,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = "Search..."
                        )
                    }
                },
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.secondary,
                navigationIcon =
                if (!viewState.inSearchMode) {
                    {
                        IconButton(onClick = { scaffoldState.drawerState.open() }) {
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
                    if (!viewState.inSearchMode) {
                        if (inDarkMode) {
                            AppIconButton(
                                icon = Icons.Default.WbSunny,
                                onClick = { viewModel.changeDarkMode(!inDarkMode) },
                                modifier = Modifier.align(alignment = Alignment.Top)
                            )
                        } else {
                            AppIconButton(
                                icon = Icons.Filled.NightsStay,
                                onClick = { viewModel.changeDarkMode(!inDarkMode) },
                                modifier = Modifier.align(alignment = Alignment.Top)
                            )
                        }
                    }
                }
            )
        },
        bodyContent = {
            MainContent(
                viewState = viewState,
                modifier = Modifier.padding(horizontal = 2.dp),
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
        Spacer(Modifier.preferredHeight(8.dp))
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

    }
}


@ExperimentalCoroutinesApi

@Composable
fun DrawerContent(
    isUserSignedIn: Boolean,
    userInfoFlow: Flow<DataState<User>>,
    userInEdit: State<User?>,
    onEditUserInfo: (User) -> Unit,
    onEditDone: (shouldSave: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
    navigationEvent: (ScreenNavigationEvents) -> Unit,
    scaffoldState: ScaffoldState,
) {
    Surface(color = MaterialTheme.colors.primary, modifier = modifier.fillMaxHeight()) {
        ScrollableColumn(
            verticalArrangement = Arrangement.Top,
        ) {

            if (!isUserSignedIn) {
                DrawerButton(text = "Login", icon = Icons.Default.Login, onclick = {
                    scaffoldState.drawerState.close(onClosed = {
                        navigationEvent(
                            ScreenNavigationEvents.NavigateToSignInActivity
                        )
                    })
                })
            } else {
                ProfileSection(
                    userInfoFlow = userInfoFlow,
                    userInEdit = userInEdit,
                    onSelectPhoto = { navigationEvent(ScreenNavigationEvents.NavigateToChoosePhotoActivity) },
                    onEditUser = onEditUserInfo,
                    onEditUserDone = onEditDone
                )
            }


            DrawerButton(text = "Favorites", icon = Icons.Default.Favorite, onclick = {
                scaffoldState.drawerState.close(onClosed = {
                    navigationEvent(
                        ScreenNavigationEvents.NavigateToFavorites
                    )
                })
            })

            if (isUserSignedIn) {
                DrawerButton(text = "Sign out", icon = Icons.Default.ExitToApp, onclick = {
                    scaffoldState.drawerState.close(onClosed = onSignOut)
                })
            }

        }


        onCommit(scaffoldState.drawerState.isOpen) {
            onEditDone(false)
        }

    }

}


@Composable
fun ProfileSection(
    userInfoFlow: Flow<DataState<User>>,
    userInEdit: State<User?>,
    onEditUser: (User) -> Unit,
    onSelectPhoto: () -> Unit,
    onEditUserDone: (shouldSave: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val userInfoState by userInfoFlow.collectAsState(null)
    userInfoState?.let { state ->
        Surface(
            elevation = 0.dp,
            color = MaterialTheme.colors.surface,
            modifier = modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            when (state) {
                is DataState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is DataState.Success -> {
                    ProfileInfo(
                        user = state.data,
                        userInEditState = userInEdit,
                        onUserInfoChange = onEditUser,
                        onSelectPhoto = onSelectPhoto,
                        onEditDone = onEditUserDone
                    )
                }
                is DataState.Failed -> {
                    Log.d(TAG, "ProfileSection: Error ${state.message}")
                    Text(text = "Error: ${state.message}")
                }
            }

        }
    }
}


@Composable
fun ProfileInfo(
    user: User,
    userInEditState: State<User?>,
    modifier: Modifier = Modifier,
    onUserInfoChange: (User) -> Unit,
    onSelectPhoto: () -> Unit,
    onEditDone: (shouldSave: Boolean) -> Unit
) {
    val userInEdit = userInEditState.value
    Column(
        modifier = modifier.padding(
            top = 8.dp,
            start = 8.dp,
            end = 8.dp
        )
    ) {
        val inEditMode = userInEdit != null
        val photoLink = if (inEditMode) userInEdit!!.pictureUri else user.pictureUri
        Row {
            ProfilePhoto(
                imagePath = photoLink,
                inEditMode = inEditMode,
                onSelectPhoto = onSelectPhoto
            )

            Spacer(modifier = Modifier.weight(1f))
            if (inEditMode) {
                ConfirmRejectButtons(
                    onConfirm = {
                        onEditDone(true)
                    }, onReject = {
                        onEditDone(false)
                    })
            }

        }
        Spacer(modifier = Modifier.preferredHeight(8.dp))
        if (inEditMode) {
            ProfileNameEditor(text = userInEdit!!.name, onTextChange = { newName ->
                val tmpUser = userInEdit.copy(name = newName)
                onUserInfoChange(tmpUser)
            })
        } else {
            ProfileName(text = user.name, onModeChange = {
                onUserInfoChange(user.copy(pictureUri = null))
            })
        }
    }
}

@Composable
fun ConfirmRejectButtons(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onReject: () -> Unit
) {
    Row(modifier = modifier) {
        IconButton(onClick = onReject) {
            Icon(Icons.Default.Cancel)
        }
        IconButton(onClick = onConfirm) {
            Icon(Icons.Default.Done)
        }
    }
}

@Composable
fun ProfilePhoto(
    imagePath: Any?,
    inEditMode: Boolean,
    onSelectPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.preferredSize(80.dp).zIndex(8f).clip(shape = CircleShape)) {
        CoilImage(
            data = imagePath ?: "",
            fadeIn = true,
            contentScale = ContentScale.Crop,
            modifier = Modifier.preferredSize(80.dp).background(MaterialTheme.colors.onSurface)
        )

        if (inEditMode) {
            Surface(
                color = Color.Transparent,
                contentColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(onClick = onSelectPhoto, modifier = Modifier.align(Alignment.Center)) {
                    Icon(Icons.Default.Camera)
                }
            }
        }
    }
}


@Composable
fun ProfileNameEditor(
    text: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit
) {
    Row(modifier = modifier) {
        MainInputText(
            text = text,
            onTextChanged = onTextChange,
            onImeAction = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Name"
        )
    }
}

@Composable
fun ProfileName(
    text: String,
    modifier: Modifier = Modifier,
    onModeChange: (Boolean) -> Unit
) {
    Row(modifier = modifier) {
        Text(text = text, modifier = Modifier.align(alignment = Alignment.CenterVertically))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onModeChange(true) },
            modifier = Modifier.align(alignment = Alignment.Top)
        ) {
            Icon(Icons.Default.Edit, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun DrawerButton(
    text: String,
    icon: ImageVector,
    onclick: () -> Unit
) {
    Button(
        onClick = onclick,
        elevation = null,
        colors = ButtonConstants.defaultButtonColors(
            backgroundColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),

        ) {
        Icon(icon, modifier = Modifier.preferredSize(20.dp))
        Spacer(modifier = Modifier.preferredWidth(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
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
                color = MaterialTheme.colors.onPrimary,
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
        backgroundColor = Color.Transparent
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
            selected -> MaterialTheme.colors.secondary.copy(alpha = 0.9f)
            else -> MaterialTheme.colors.secondaryVariant.copy(alpha = 0.412f)
        },
        contentColor = MaterialTheme.colors.onSecondary,
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
            ),
        backgroundColor = MaterialTheme.colors.secondary
    ) {
        Column(modifier = Modifier) {
            val imageUrl = if (item.posterPath.isNotBlank()) {
                ImageConfig.profilePath + item.posterPath
            } else {
                //just a placeholder for demo
                "https://critics.io/img/movies/poster-placeholder.png"
            }

            Log.d(TAG, "TmdbItem: $imageUrl")
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


