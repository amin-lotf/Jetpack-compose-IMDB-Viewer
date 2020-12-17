package com.example.imdbviewer.data.network.tmdb.mappers

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.network.tmdb.models.item.movie.TmdbMovieListItemDto
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.domain_models.TmdbListItem
import com.example.imdbviewer.util.mapToYear
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListItemMapper @Inject constructor(): DomainMapper<TmdbMovieListItemDto, TmdbListItem> {

    override fun mapToDomainModel(from: TmdbMovieListItemDto): TmdbListItem {
        return TmdbListItem(
            id=from.id,
            posterPath = from.posterPath?:"",
            title = from.title?:"",
            voteAverage = from.voteAverage?:0.0,
            year = from.year?.mapToYear()?:"",
            category = CategoryType.Movies.label,
            isSynced = false
        )
    }

    override fun mapFromDoaminModel(from: TmdbListItem): TmdbMovieListItemDto? {
        // No need in this project
        return null
    }
}