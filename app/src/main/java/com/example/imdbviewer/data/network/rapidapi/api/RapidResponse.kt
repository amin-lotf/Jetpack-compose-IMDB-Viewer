package com.example.imdbviewer.data.network.rapidapi.api

import com.example.imdbviewer.models.RapidItem
import com.example.imdbviewer.models.RapidTV
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract  class RapidResponse(
    open val items: List<RapidItem>?,
    open val status:String?,
    open val statusMessage:String?,
    open val results:Int?,
    open val totalItems:Int?
)