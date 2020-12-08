package com.example.imdbviewer.ui.detailscreen

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imdbviewer.models.tmdb.genres.Genre
import com.example.imdbviewer.models.tmdb.people.Cast
import com.example.imdbviewer.util.RowLayout
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.compose.runtime.getValue
import com.example.imdbviewer.models.tmdb.item.TmdbItemDetails
import com.example.imdbviewer.util.ImageConfig

@ExperimentalCoroutinesApi
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    // navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.state.collectAsState()
    val tmdbItem=viewState.itemDetails
    if (tmdbItem!=null){
        ScrollableColumn(modifier = modifier) {
            Box(modifier = Modifier.preferredHeight(200.dp).fillMaxWidth()) {
                CoilImage(
                    data = ImageConfig.backdropPath + tmdbItem.backdropPath,
                    fadeIn = true,
                    contentScale = ContentScale.FillBounds,
                )
                Surface(
                    color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                    modifier = Modifier.align(Alignment.BottomStart)
                        .fillMaxWidth()
                ) {
                    ShowMetadata(tmdbItem = tmdbItem)
                }
            }
            ShowDescription(tmdbItem = tmdbItem)
            Spacer(modifier = Modifier.preferredHeight(16.dp))
            ShowCredits(tmdbItem = tmdbItem)

        }
    }
    else{
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
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
fun PersonItem(person: Cast, modifier: Modifier = Modifier) {
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
                text = person.name?:"",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = person.character?:"",
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
fun ShowMetadata(tmdbItem: TmdbItemDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = tmdbItem.title,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface
        )
//        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
//            Text(
//                "TG-130 | 1h 30min",
//                style = MaterialTheme.typography.subtitle2
//            )
//        }

    }
}

