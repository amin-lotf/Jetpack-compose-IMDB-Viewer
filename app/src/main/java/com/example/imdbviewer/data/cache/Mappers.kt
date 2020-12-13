package com.example.imdbviewer.data.cache

import com.example.imdbviewer.firebase.model.FirebaseUserMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mappers @Inject constructor(
    val firebaseUserMapper:FirebaseUserMapper
)