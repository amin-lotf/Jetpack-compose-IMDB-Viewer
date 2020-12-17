package com.example.imdbviewer.domain_models

import android.net.Uri

data class User(
    val name:String,
    val profilePicturePath:String?, //  reference to the stored photo
    val pictureUri:Uri?=null //  reference to the new chosen photo
)