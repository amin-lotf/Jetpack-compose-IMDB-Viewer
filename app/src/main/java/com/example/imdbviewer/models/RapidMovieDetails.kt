package com.example.imdbviewer.models

import com.google.gson.annotations.SerializedName

data class RapidMovieDetails (
    @SerializedName(  "imdb_id")
    val imdbID: String,
    val title: String?,
    val description: String?,
    val year: String?,
    @SerializedName( "release_date")
    val releaseDate: String?,
    @SerializedName(  "imdb_rating")
    val imdbRating: String?,
    val genres: List<String>?,
    @SerializedName(  "vote_count")
    val voteCount: String?,
    val popularity: String?,
    val rated: String?,
    val runtime: Long?,
    val stars: List<String>?,
    val directors: List<String>?,
    val countries: List<String>?,
    val language: List<String>?,
    val status: String?,
    @SerializedName(  "status_message")
    val statusMessage: String?
)