package com.example.imdbviewer.models

import android.net.Uri

data class User(
    val name:String,
    val profilePicturePath:String?, //  reference to the stored photo
    val localPicturePath:Uri?=null //  reference to the new chosen photo
)