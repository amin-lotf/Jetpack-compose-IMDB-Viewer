package com.example.imdbviewer.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val BlueThemeLight = lightColors(
    primary = Color.White,
    primaryVariant = pink600,
    onPrimary = Color.Black,
    secondary = blue800,
    secondaryVariant = blueGray900,
    error = Red200,
    onSecondary = Color.White,
    surface = blue800,
    onSurface = Color.White,
)


private val DarkColors = darkColors(
    primary = blueGray700,
    primaryVariant = Red300,
    onPrimary = Color.White,
    secondary = blueGray900,
    error = Red200,
    onSecondary = Color.White,
    surface = blueGray900,
    onSurface = Color.White,

    )


@Composable
fun IMDBViewerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        BlueThemeLight
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}