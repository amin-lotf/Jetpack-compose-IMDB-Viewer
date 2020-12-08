package com.example.imdbviewer.models.tmdb.item

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class TmdbListItem(
     val id : Int,
     val originalTitle : String,
     val posterPath : String,
     val title : String,
     val voteAverage : Double,
     val year : String,
)