package com.example.imdbviewer.ui.detailscreen

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.imdbviewer.theme.IMDBViewerTheme
import com.example.imdbviewer.ui.mainscreen.MainScreen
import com.example.imdbviewer.ui.mainscreen.MainViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Composable
fun DetailScreen(modifier: Modifier = Modifier) {

    ScrollableColumn(modifier = modifier) {
        Box(modifier = Modifier.preferredHeight(200.dp).fillMaxWidth()) {
            CoilImage(
                data = "https://image.tmdb.org/t/p/original/s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
                fadeIn = true,
                contentScale = ContentScale.FillBounds,
            )
            Surface(
                color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                ShowMetadata()
            }

        }

        ShowDescription()


    }

}

@Composable
fun ShowDescription(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.preferredHeight(8.dp))
        Text(
            text =
            "Genres:",
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary
        )
        LazyRowFor(
            items = listOf("Action", "Adventure", "Sci-Fi"),
            contentPadding = PaddingValues(start = 4.dp, end = 4.dp)
        ) { item ->
            Surface(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                modifier = Modifier.padding(horizontal = 4.dp,vertical = 4.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = item,modifier = Modifier.padding(horizontal = 8.dp,vertical = 4.dp))
            }

        }
    }
}

@Composable
fun ShowMetadata(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "The Inception (2010)",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface
        )
        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
            Text(
                "TG-130 | 1h 30min",
                style = MaterialTheme.typography.subtitle2
            )
        }

    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
fun DetailScreenPreview() {
    IMDBViewerTheme(darkTheme = true) {

        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxHeight()) {
            DetailScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}