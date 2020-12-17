package com.example.imdbviewer.ui.detailscreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imdbviewer.data.network.tmdb.models.people.CastDto
import com.example.imdbviewer.util.RowLayout
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.imdbviewer.domain_models.TmdbItemDetails
import com.example.imdbviewer.util.ImageConfig

private val imageHeight = 300.dp
private val metadataHeight = 50.dp
private val metadataOffset = imageHeight - metadataHeight

@ExperimentalCoroutinesApi
@Composable
fun DetailsScreen(
    viewModel: DetailViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.state.collectAsState()

    val tmdbItem = viewState.itemDetails
    Box(modifier = modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0f)
        tmdbItem?.let {
            Header(imagePath = tmdbItem.backdropPath)
            Up(upPress = { navController.popBackStack() })
            TmdbMetadata(tmdbItem = tmdbItem, saveItem = viewModel::updateFavoriteState)
            DetailsContent(tmdbItem = tmdbItem, scrollState = scroll)
        }

    }


}


@Composable
fun Header(imagePath: String?, modifier: Modifier = Modifier) {
    val imageUrl = imagePath?.let { ImageConfig.backdropPath + it }
        ?: "https://critics.io/img/movies/poster-placeholder.png"
    CoilImage(
        data = imageUrl,
        fadeIn = true,
        contentScale = ContentScale.FillHeight,
        modifier = modifier.preferredHeight(imageHeight)
    )

}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .preferredSize(36.dp)
            .background(
                color = MaterialTheme.colors.surface.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            Icons.Outlined.ArrowBack,
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
fun DetailsContent(
    tmdbItem: TmdbItemDetails,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {

    Column {
        Spacer(modifier = Modifier.preferredHeight(imageHeight).fillMaxWidth())
        ScrollableColumn(modifier = modifier, scrollState = scrollState) {
            ShowDescription(tmdbItem = tmdbItem)
            Spacer(modifier = Modifier.preferredHeight(16.dp))
            ShowCredits(tmdbItem = tmdbItem)

        }
    }
//    else{
//        Box(modifier = Modifier.fillMaxSize()) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }
//    }
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
            color = MaterialTheme.colors.primary
        )
        LazyRowFor(
            items = tmdbItem.genres,
            contentPadding = PaddingValues(start = 4.dp, end = 4.dp)
        ) { item ->
            Surface(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
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


@Composable
fun ShowCredits(tmdbItem: TmdbItemDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text =
            "Director:",
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
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
            items = tmdbItem.cast,
            widthFraction = .3f
        ) { person ->
            PersonItem(
                person = person,
                modifier = Modifier.padding(2.dp).preferredHeight(220.dp)
            )
        }

    }
}


@Composable
fun PersonItem(person: CastDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clip(shape = RoundedCornerShape(8.dp)),
        elevation = 0.dp
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
                contentScale = ContentScale.Fit,
                modifier = Modifier.preferredWidth(92.dp).preferredHeight(138.dp)
                    .clip(RoundedCornerShape(8.dp))
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
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .preferredHeight(metadataOffset)
        )
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier.preferredHeight(metadataHeight)
                    .padding(vertical = 8.dp)
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                Text(
                    text = tmdbItem.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                )

                IconButton(
                    onClick = {
                        saveItem(tmdbItem, !tmdbItem.isFavorite)
                    },
                    modifier = Modifier.preferredSize(36.dp).align(Alignment.CenterVertically)
                ) {
                    Surface(
                        color = Color.Transparent,
                        contentColor = if (tmdbItem.isFavorite) Color.Red else MaterialTheme.colors.onSurface) {
                            Icon(Icons.Sharp.Favorite.copy())
                        }
                }
//        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
//            Text(
//                "TG-130 | 1h 30min",
//                style = MaterialTheme.typography.subtitle2
//            )
//        }
            }
        }
    }
}

