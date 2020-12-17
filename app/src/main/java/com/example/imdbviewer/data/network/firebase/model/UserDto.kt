package com.example.imdbviewer.data.network.firebase.model

data class UserDto(
    val name:String,
    val profilePicturePath:String?
) {
    constructor():this("",null)
}