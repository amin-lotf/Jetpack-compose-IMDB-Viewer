package com.example.imdbviewer.ui.detailscreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imdbviewer.data.network.tmdb.models.people.CastDto
import com.example.imdbviewer.util.RowLayout
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.imdbviewer.domain_models.TmdbItemDetails
import com.example.imdbviewer.ui.mainscreen.AppIconButton
import com.example.imdbviewer.util.ImageConfig
import com.example.imdbviewer.util.ImageConfig.personPosterHeight
import com.example.imdbviewer.util.ImageConfig.personPosterWidth


private const val imageHeight = 1280
private val metadataHeight = 50.dp

//private val metadataOffset = imageHeight - metadataHeight
private val aspectRation = (1280f / 720).coerceAtLeast(1f)

@ExperimentalCoroutinesApi
@Composable
fun DetailsScreen(
    viewModel: DetailViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.state.collectAsState()

    val tmdbItem = viewState.itemDetails
    WithConstraints {

        val scroll = rememberScrollState(initial = 10f)
        Box(modifier = modifier.fillMaxSize().padding(vertical = 0.dp)) {

            tmdbItem?.let {
                DetailsContent(
                    tmdbItem = tmdbItem,
                    scrollState = scroll,
                    containerHeight = maxHeight
                )

                TopBar(
                    upPress = { navController.popBackStack() },
                    tmdbItem = tmdbItem,
                    saveItem = viewModel::updateFavoriteState,
                    scrollState = scroll
                )
            }
        }
    }
}


@Composable
fun Header(imagePath: String?, modifier: Modifier = Modifier, scrollState: ScrollState) {
    val imageUrl = imagePath?.let { ImageConfig.backdropPath + it }
        ?: "https://critics.io/img/movies/poster-placeholder.png"

    val offset = scrollState.value / 1.3f
    val offsetDp = with(AmbientDensity.current) {
        offset.toDp()
    }
    val imageHeightDp = with(AmbientDensity.current) { imageHeight.toDp() }

    CoilImage(
        data = imageUrl,
        fadeIn = true,
        contentScale = ContentScale.FillWidth,
        modifier = modifier.aspectRatio(aspectRation).preferredHeight(imageHeightDp)
            .padding(top = offsetDp)
    )

}

@Composable
private fun TopBar(
    tmdbItem: TmdbItemDetails,
    saveItem: (TmdbItemDetails, Boolean) -> Unit,
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val alpha = (scrollState.value / 500f).coerceAtLeast(.4f).coerceAtMost(1f)
    Surface(
        color = MaterialTheme.colors.secondary.copy(alpha = alpha),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row {
            AppIconButton(icon = Icons.Outlined.ArrowBack, onClick = upPress)
            TmdbMetadata(tmdbItem = tmdbItem, saveItem = saveItem)
        }
    }

}

@ExperimentalCoroutinesApi
@Composable
fun DetailsContent(
    tmdbItem: TmdbItemDetails,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    containerHeight: Dp
) {

    ScrollableColumn(modifier = modifier.padding(vertical = 0.dp), scrollState = scrollState) {
        Header(imagePath = tmdbItem.backdropPath, scrollState = scrollState)
        ShowDescription(tmdbItem = tmdbItem)
        Spacer(modifier = Modifier.preferredHeight(16.dp))
        ShowCredits(tmdbItem = tmdbItem)
        Spacer(Modifier.preferredHeight((containerHeight - 320.dp).coerceAtLeast(0.dp)))

    }
}


@Composable
fun ShowDescription(tmdbItem: TmdbItemDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = tmdbItem.details,
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.preferredHeight(8.dp))
        Text(
            text =
            "Genres:",
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primaryVariant
        )
        LazyRow(
            contentPadding = PaddingValues(start = 4.dp, end = 4.dp)
        ) {
            items(tmdbItem.genres) { item ->
                Surface(
                    color = MaterialTheme.colors.surface.copy(alpha = 0.2f),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ShowCredits(tmdbItem: TmdbItemDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text =
            "Director:",
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primaryVariant
        )
        Text(
            text = tmdbItem.director.firstOrNull() ?: "",
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.preferredHeight(16.dp))
        Text(
            text = "Cast:",
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.preferredHeight(8.dp))



        RowLayout(
            items = tmdbItem.cast
        ) { person ->
            PersonItem(
                person = person,
                modifier = Modifier
                    .padding(2.dp)
                    .preferredHeight(personPosterHeight)
                    .preferredWidth(personPosterWidth)
            )
        }

    }
}


@Composable
fun PersonItem(person: CastDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clip(shape = RoundedCornerShape(8.dp)),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.secondary
    ) {
        Column {
            val imageUrl = if (!person.profile_path.isNullOrBlank()) {
                ImageConfig.profilePath + person.profile_path
            } else {
                //just a placeholder for demo
                "https://critics.io/img/movies/poster-placeholder.png"
            }
            CoilImage(
                data = imageUrl,
                fadeIn = true,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxWidth().preferredHeight(138.dp)

            )
            Text(
                text = person.name ?: "",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = person.character ?: "",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}


@Composable
fun TmdbMetadata(
    tmdbItem: TmdbItemDetails,
    saveItem: (TmdbItemDetails, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.preferredHeight(metadataHeight)
            .padding(vertical = 8.dp)
            .padding(start = 16.dp, end = 8.dp)
    ) {
        Text(
            text = tmdbItem.title,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(
            onClick = {
                saveItem(tmdbItem, !tmdbItem.isFavorite)
            },
            modifier = Modifier.preferredSize(36.dp).align(Alignment.CenterVertically)
        ) {
            Surface(
                color = Color.Transparent,
                contentColor = if (tmdbItem.isFavorite) Color.Red else MaterialTheme.colors.onSurface
            ) {
                Icon(Icons.Sharp.Favorite.copy())
            }
        }
    }

}

