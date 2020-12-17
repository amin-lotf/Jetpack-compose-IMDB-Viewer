package com.example.imdbviewer.data.cache.mappers

import com.example.imdbviewer.data.cache.models.ListItemEntity
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.domain_models.TmdbListItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListItemMapper @Inject constructor():DomainMapper<ListItemEntity,TmdbListItem> {

    override fun mapToDomainModel(from: ListItemEntity): TmdbListItem {
        return TmdbListItem(
            id = from.id,
            posterPath = from.posterPath,
            title = from.title,
            voteAverage = from.voteAverage,
            year = from.year,
            category = from.category,
            isSynced = from.isSynced
        )
    }

    override fun mapFromDoaminModel(from: TmdbListItem): ListItemEntity {
        return ListItemEntity(
            id = from.id,
            posterPath = from.posterPath,
            title = from.title,
            voteAverage = from.voteAverage,
            year = from.year,
            category = from.category,
            isSynced = from.isSynced,
            timeAdded = 0
        )
    }
}