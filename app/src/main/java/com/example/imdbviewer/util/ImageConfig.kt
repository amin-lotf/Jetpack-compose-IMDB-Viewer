package com.example.imdbviewer.util

object ImageConfig {
    private const val BASE_URL = "https://image.tmdb.org/t/p/"
    private const val LOGO_SIZE = "w45"
    private const val POSTER_SIZE = "w154"
    private const val PROFILE_SIZE = "w185"
    private const val BACKDROP_SIZE = "w300"

    val posterPath: String
        get() = "$BASE_URL$POSTER_SIZE"

    val profilePath: String
        get() = "$BASE_URL$PROFILE_SIZE"

    val backdropPath: String
        get() = "$BASE_URL$BACKDROP_SIZE"

    val logoPath:String
        get() = "$BASE_URL$LOGO_SIZE"
}