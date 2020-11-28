package com.example.imdbviewer.models

import androidx.room.Ignore


abstract class RapidItem(
    @Ignore
    open val imdbId:String,
    @Ignore
    open var title:String,
    @Ignore
    open var year:Int
)