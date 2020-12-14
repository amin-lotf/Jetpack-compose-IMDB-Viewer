package com.example.imdbviewer.ui.mainscreen

import com.example.imdbviewer.data.state.DataState
import com.example.imdbviewer.models.User


data class UserViewState(
    val user:DataState<User>? = null,
    val isSignedIn:Boolean=false,
    val inEditMode:Boolean=false,
    val shouldSave:Boolean=false
)