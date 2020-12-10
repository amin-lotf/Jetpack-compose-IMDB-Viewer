package com.example.imdbviewer.util

import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.models.tmdb.item.TmdbItemDetails
import com.example.imdbviewer.models.tmdb.item.TmdbListItem
import com.example.imdbviewer.models.tmdb.item.movie.TmdbMovieDetails
import com.example.imdbviewer.models.tmdb.item.movie.TmdbMovieListItem
import com.example.imdbviewer.models.tmdb.item.tv.TmdbTVDetails
import com.example.imdbviewer.models.tmdb.item.tv.TmdbTVListItem

fun <T> mapToItemDetails(item:T,type:CategoryType): TmdbItemDetails {
    try {
        return when (item) {
            is TmdbMovieDetails -> {
                val director =
                    item.credits?.crew?.filter { it.job == "Director" }?.map { it.name }
                TmdbItemDetails(
                    id = item.id,
                    title = item.title?:"",
                    details = item.overview?:"",
                    genres = item.genres?: emptyList(),
                    director = director?: emptyList(),
                    cast = item.credits?.cast?.sortedBy { it.order }?: emptyList(),
                    posterPath = item.poster_path?:"",
                    backdropPath = item.backdrop_path?:"",
                    releaseYear = mapToYear(item.release_date),
                    duration = item.runtime?:0,
                    voteAverage = item.vote_average?:0.0,
                    voteCount = item.vote_count?:0,
                    isFavorite = item.isFavorite,
                    category = type.label
                )
            }

            is TmdbTVDetails -> {
                TmdbItemDetails(
                    id = item.id,
                    title = item.name?:"",
                    details = item.overview?:"",
                    genres = item.genres?: emptyList(),
                    director = item.created_by?.map { it.name }?: emptyList(),
                    cast = item.credits?.cast?.sortedBy { it.order }?: emptyList(),
                    posterPath = item.poster_path?:"",
                    backdropPath = item.backdrop_path?:"",
                    releaseYear = mapToYear(item.first_air_date),
                    duration = 0,
                    voteAverage = item.vote_average?:0.0,
                    voteCount = item.vote_count?:0,
                    isFavorite = item.isFavorite,
                    category = type.label
                )
            }

            else -> throw IllegalArgumentException("Invalid cast")
        }
    }catch (throwable:Throwable){
        throw throwable
    }
}


fun <T> mapToListItem(item:T,type:CategoryType):TmdbListItem{
    try {

        return when(item){
            is TmdbMovieListItem->{
                TmdbListItem(
                    id=item.id,
                    posterPath = item.posterPath?:"",
                    title = item.title?:"",
                    voteAverage = item.voteAverage?:0.0,
                    year = mapToYear(item.year),
                    category = type.label
                )
            }
            is TmdbTVListItem->{
                TmdbListItem(
                    id=item.id,
                    posterPath = item.posterPath?:"",
                    title = item.title?:"",
                    voteAverage = item.voteAverage?:0.0,
                    year = mapToYear(item.year),
                    category = type.label
                )
            }
            else -> throw IllegalArgumentException("Invalid cast")
        }


    }catch (throwable:Throwable){
        throw throwable
    }
}

fun mapToYear(date:String?):String{
    if (date.isNullOrBlank()) return ""
    return if (date.length>4){
        date.substring(0,4)
    }else{
        date
    }
}