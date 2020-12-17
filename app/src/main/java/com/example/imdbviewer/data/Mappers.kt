package com.example.imdbviewer.data

import com.example.imdbviewer.data.cache.mappers.ListItemMapper
import com.example.imdbviewer.data.network.firebase.mappers.FirestoreCacheMapper
import com.example.imdbviewer.data.network.firebase.mappers.UserMapper
import com.example.imdbviewer.data.network.tmdb.mappers.MovieDetailsMapper
import com.example.imdbviewer.data.network.tmdb.mappers.MovieListItemMapper
import com.example.imdbviewer.data.network.tmdb.mappers.TvDetailsMapper
import com.example.imdbviewer.data.network.tmdb.mappers.TvListItemMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mappers @Inject constructor(
    val userMapper: UserMapper,
    val movieDetailsMapper: MovieDetailsMapper,
    val tvDetailsMapper: TvDetailsMapper,
    val movieListItemMapper: MovieListItemMapper,
    val tvListItemMapper: TvListItemMapper,
    val listItemMapper: ListItemMapper,
    val firestoreCacheMapper: FirestoreCacheMapper
)