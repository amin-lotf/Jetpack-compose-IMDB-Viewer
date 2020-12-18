package com.example.imdbviewer.domain_models

import com.example.imdbviewer.data.network.tmdb.models.genres.GenreDto
import com.example.imdbviewer.data.network.tmdb.models.people.CastDto


data class TmdbItemDetails(
    val id: Int,
    val title: String,
    val details: String,
    val genres: List<GenreDto>,
    val director: List<String>,
    val cast: List<CastDto>,
    val posterPath: String,
    val backdropPath: String,
    val releaseYear: String,
    val duration: Int,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean,
    val category: String,
)