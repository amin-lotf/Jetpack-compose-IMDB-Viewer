package com.example.imdbviewer.ui.mainscreen

import com.example.imdbviewer.models.User

data class UserState(
    val user:User?=null,
    val isSignedIn:Boolean=false,
    val inEditMode:Boolean=false,
    val shouldSave:Boolean=false
)