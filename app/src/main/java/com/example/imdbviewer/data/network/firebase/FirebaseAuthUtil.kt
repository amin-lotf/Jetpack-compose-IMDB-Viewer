package com.example.imdbviewer.data.network.firebase

import android.util.Log
import com.example.imdbviewer.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthUtil {
    private val TAG="aminjoon"
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val userId: String
        get() = firebaseAuth.uid ?: throw NullPointerException("UID not exist")

    private val _providers=arrayListOf(
    AuthUI.IdpConfig.EmailBuilder().build(),
    AuthUI.IdpConfig.GoogleBuilder().build()
    )

    val authIntent=AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(_providers)
        .setLogo(R.drawable.logo)
        .build()

    val isUserSignedIn= firebaseAuth.currentUser!=null


    fun signOut(){
        firebaseAuth.signOut()
    }

}