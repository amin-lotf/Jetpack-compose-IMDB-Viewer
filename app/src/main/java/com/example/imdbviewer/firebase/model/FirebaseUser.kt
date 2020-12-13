package com.example.imdbviewer.firebase.model

data class FirebaseUser(
    val name:String,
    val profilePicturePath:String?
) {
    constructor():this("",null)
}