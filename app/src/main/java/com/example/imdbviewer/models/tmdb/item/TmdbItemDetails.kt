package com.example.imdbviewer.models.tmdb.item

import com.example.imdbviewer.models.tmdb.genres.Genre
import com.example.imdbviewer.models.tmdb.people.Cast

data class TmdbItemDetails(
    val id:Int,
    val title:String,
    val details:String,
    val genres:List<Genre>,
    val director:List<String>,
    val cast:List<Cast>,
    val backdropPath:String,
    val releaseYear:String,
    val duration:Int,
    val voteAverage:Double,
    val voteCount:Int,
)