package com.example.imdbviewer.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tmdb_items")
data class ListItemEntity(
    @PrimaryKey
    val id : Int,
    val posterPath : String,
    val title : String,
    val voteAverage : Double,
    val year : String,
    val category:String,
    var isSynced:Boolean,
    var timeAdded:Long
)