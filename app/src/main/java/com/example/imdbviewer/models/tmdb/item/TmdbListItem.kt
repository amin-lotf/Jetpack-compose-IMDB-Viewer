package com.example.imdbviewer.models.tmdb.item

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "tmdb_items")
data class TmdbListItem(
     @PrimaryKey
     val id : Int,
     val posterPath : String,
     val title : String,
     val voteAverage : Double,
     val year : String,
     val category:String
)