package com.example.imdbviewer.data.network.firebase.mappers

import com.example.imdbviewer.data.network.firebase.model.UserDto
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.domain_models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor(): DomainMapper<UserDto, User> {

    override fun mapToDomainModel(from: UserDto): User {
        return User(
            name = from.name,
            profilePicturePath = from.profilePicturePath
        )
    }

    override fun mapFromDoaminModel(from: User): UserDto {
        return UserDto(
            name = from.name,
            profilePicturePath = from.profilePicturePath
        )
    }
}