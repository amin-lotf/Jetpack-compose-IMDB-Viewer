package com.example.imdbviewer.data.network.api

import com.example.imdbviewer.models.RapidTV
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract  class RapidResponse<T>(
    open val items: List<T>?,
    open val status:String?,
    open val statusMessage:String?,
    open val results:Int?,
    open val totalItems:Int?
)