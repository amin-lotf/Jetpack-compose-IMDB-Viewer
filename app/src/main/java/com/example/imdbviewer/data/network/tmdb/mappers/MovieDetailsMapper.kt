package com.example.imdbviewer.data.network.tmdb.mappers

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.network.tmdb.models.item.movie.TmdbMovieDetailsDto
import com.example.imdbviewer.data.util.DomainMapper
import com.example.imdbviewer.domain_models.TmdbItemDetails
import com.example.imdbviewer.util.mapToYear
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsMapper @Inject constructor() :
    DomainMapper<TmdbMovieDetailsDto, TmdbItemDetails> {


    override fun mapToDomainModel(from: TmdbMovieDetailsDto): TmdbItemDetails {
        val director =
            from.credits?.crew?.filter { it.job == "Director" }?.map { it.name }
        return TmdbItemDetails(
            id = from.id,
            title = from.title ?: "",
            details = from.overview ?: "",
            genres = from.genres ?: emptyList(),
            director = director ?: emptyList(),
            cast = from.credits?.cast?.sortedBy { it.order } ?: emptyList(),
            posterPath = from.poster_path ?: "",
            backdropPath = from.backdropPath ?: "",
            releaseYear = from.release_date?.mapToYear() ?: "",
            duration = from.runtime ?: 0,
            voteAverage = from.vote_average ?: 0.0,
            voteCount = from.vote_count ?: 0,
            isFavorite = from.isFavorite,
            category = CategoryType.Movies.label
        )
    }

    override fun mapFromDomainModel(from: TmdbItemDetails): TmdbMovieDetailsDto? {
        //No Need in this project
        return null
    }
}

