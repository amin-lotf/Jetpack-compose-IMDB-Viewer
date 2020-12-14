package com.example.imdbviewer.data.network.firebase.model

import com.example.imdbviewer.data.network.util.EntityMapper
import com.example.imdbviewer.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserMapper @Inject constructor(): EntityMapper<FirebaseUser, User> {

    override fun mapFromEntity(entity: FirebaseUser): User {
        return User(
            name = entity.name,
            profilePicturePath = entity.profilePicturePath
        )
    }

    override fun mapToEntity(domainModel: User): FirebaseUser {
        return FirebaseUser(
            name = domainModel.name,
            profilePicturePath = domainModel.profilePicturePath
        )
    }
}