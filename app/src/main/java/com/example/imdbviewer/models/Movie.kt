package com.example.imdbviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class Movie(
        @Expose
        @PrimaryKey
        @SerializedName("id")
        val id:String,
        @Expose
        @SerializedName("title")
        val title:String,
        var category:String?,
        @Expose
        @SerializedName("year")
        val year:Int,
        @Expose
        @SerializedName("image")
        val thumbnail:String=""
)