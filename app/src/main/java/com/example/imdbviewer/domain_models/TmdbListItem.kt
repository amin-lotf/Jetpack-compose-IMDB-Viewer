package com.example.imdbviewer.domain_models

import androidx.room.Entity
import androidx.room.PrimaryKey


data class TmdbListItem(

     val id : Int,
     val posterPath : String,
     val title : String,
     val voteAverage : Double,
     val year : String,
     val category:String,
     val isSynced:Boolean
)