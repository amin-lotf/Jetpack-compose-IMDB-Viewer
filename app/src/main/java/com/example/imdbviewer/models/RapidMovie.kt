package com.example.imdbviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rapid_movies")
data class RapidMovie(
    @Expose
    @SerializedName("imdb_id")
    @PrimaryKey
    override val imdbId:String,

    @Expose
    override var title:String,

    @Expose
    override var year:Int
):RapidItem(imdbId,title,year)