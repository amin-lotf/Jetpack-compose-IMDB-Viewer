package com.example.imdbviewer.data.network.tmdb.mappers

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.network.tmdb.models.item.tv.TmdbTVListItemDto
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.domain_models.TmdbListItem
import com.example.imdbviewer.util.mapToYear
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvListItemMapper @Inject constructor() : DomainMapper<TmdbTVListItemDto, TmdbListItem> {

    override fun mapToDomainModel(from: TmdbTVListItemDto): TmdbListItem {
        return TmdbListItem(
            id = from.id,
            posterPath = from.posterPath ?: "",
            title = from.title ?: "",
            voteAverage = from.voteAverage ?: 0.0,
            year = from.year?.mapToYear() ?: "",
            category = CategoryType.TVs.label,
            isSynced = false
        )
    }

    override fun mapFromDomainModel(from: TmdbListItem): TmdbTVListItemDto? {
        // No need in this project
        return null
    }
}