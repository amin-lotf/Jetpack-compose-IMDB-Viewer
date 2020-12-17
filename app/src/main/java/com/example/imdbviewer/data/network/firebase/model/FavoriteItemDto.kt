package com.example.imdbviewer.data.network.firebase.model

data class FavoriteItemDto(
    val id : Int,
    val posterPath : String,
    val title : String,
    val voteAverage : Double,
    val year : String,
    val category:String,
    var timeAdded:Long,
    var timeSynced:Long?
){
    constructor():this(0,"",",",0.0,"","",0,0)
}