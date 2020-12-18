package com.example.imdbviewer.data.network.tmdb.api


abstract class TmdbListResponse<T>(
    open val results: List<T>,
    open val totalResults: Int,
    open val page: Int,
    open val totalPages: Int
)