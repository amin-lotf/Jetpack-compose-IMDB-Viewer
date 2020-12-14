package com.example.imdbviewer.data.network.firebase.model

data class FirebaseUser(
    val name:String,
    val profilePicturePath:String?
) {
    constructor():this("",null)
}