package com.example.imdbviewer.data.network.tmdb.mappers

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.network.tmdb.models.item.tv.TmdbTVDetailsDto
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.domain_models.TmdbItemDetails
import com.example.imdbviewer.util.mapToYear
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvDetailsMapper @Inject constructor() : DomainMapper<TmdbTVDetailsDto, TmdbItemDetails> {


    override fun mapToDomainModel(from: TmdbTVDetailsDto): TmdbItemDetails {

        return TmdbItemDetails(
            id = from.id,
            title = from.name ?: "",
            details = from.overview ?: "",
            genres = from.genres ?: emptyList(),
            director = from.created_by?.map { it.name } ?: emptyList(),
            cast = from.credits?.cast?.sortedBy { it.order } ?: emptyList(),
            posterPath = from.poster_path ?: "",
            backdropPath = from.backdrop_path ?: "",
            releaseYear = from.first_air_date?.mapToYear() ?: "",
            duration = 0,
            voteAverage = from.vote_average ?: 0.0,
            voteCount = from.vote_count ?: 0,
            isFavorite = from.isFavorite,
            category = CategoryType.TVs.label
        )
    }

    override fun mapFromDomainModel(from: TmdbItemDetails): TmdbTVDetailsDto? {
        //No Need in this project
        return null
    }
}

