package com.example.imdbviewer.data.network

import com.example.imdbviewer.models.Movie
import com.google.gson.annotations.Expose

class ImdbResponse(
    @Expose
    val items:List<Movie>,
    @Expose
    val errorMessage:String
)