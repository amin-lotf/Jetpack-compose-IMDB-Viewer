package com.example.imdbviewer.data.network.firebase.mappers

import com.example.imdbviewer.data.cache.models.ListItemEntity
import com.example.imdbviewer.data.network.firebase.model.FavoriteItemDto
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.data.util.DtoEntityMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreCacheMapper @Inject constructor():DtoEntityMapper<ListItemEntity,FavoriteItemDto>{
    override fun mapToDto(entity: ListItemEntity): FavoriteItemDto {
        return FavoriteItemDto(
            id = entity.id,
            posterPath = entity.posterPath,
            title = entity.title,
            voteAverage = entity.voteAverage,
            year = entity.year,
            category = entity.category,
            timeAdded = entity.timeAdded,
            timeSynced = null
        )
    }

    override fun mapToEntity(dto: FavoriteItemDto): ListItemEntity {
        return ListItemEntity(
            id = dto.id,
            posterPath = dto.posterPath,
            title = dto.title,
            voteAverage = dto.voteAverage,
            year = dto.year,
            category = dto.category,
            timeAdded = dto.timeAdded,
            isSynced = true
        )
    }
}