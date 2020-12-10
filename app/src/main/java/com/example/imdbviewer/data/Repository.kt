package com.example.imdbviewer.data

import androidx.paging.*
import com.example.imdbviewer.data.cache.*
import com.example.imdbviewer.data.network.tmdb.TMDbPagingSource
import com.example.imdbviewer.data.network.tmdb.api.TmdbApi
import com.example.imdbviewer.models.tmdb.item.TmdbItemDetails
import com.example.imdbviewer.models.tmdb.item.TmdbListItem
import com.example.imdbviewer.util.TMDB_KEY
import com.example.imdbviewer.util.mapToItemDetails
import com.example.imdbviewer.util.mapToListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val tmdbDao: TmdbDao
) {
    val TAG = "aminjoon"

    fun getSavedItems()=
        tmdbDao.getFavorites()

    suspend fun updateTmdbFavoriteState(tmdbItemDetails: TmdbItemDetails,isFavorite:Boolean){
        if (isFavorite){
            val tmdbItem=TmdbListItem(
                id = tmdbItemDetails.id,
                title = tmdbItemDetails.title,
                posterPath = tmdbItemDetails.posterPath,
                voteAverage = tmdbItemDetails.voteAverage,
                year = tmdbItemDetails.releaseYear,
                category = tmdbItemDetails.category
            )
            tmdbDao.insertTmdbItem(tmdbItem)
        }else{
            tmdbDao.deleteItem(tmdbItemDetails.id)
        }
    }


     fun getTmdbItemDetails(itemId: Int, type: CategoryType):Flow<TmdbItemDetails> {
        val tmdbItemFlow=flow {
            val response = when (type) {
                CategoryType.Movies -> tmdbApi.getMovieDetails(
                    movieId = itemId,
                    apiKey = TMDB_KEY
                )
                CategoryType.TVs -> tmdbApi.getTVDetails(
                    tvId = itemId,
                    apiKey = TMDB_KEY
                )
            }
            val details = response.body()

            emit(mapToItemDetails(details,type))
        }
        return combine(
            tmdbItemFlow,
            tmdbDao.getItem(itemId).map { it!=null }
        ){tmdbItem,isSaved->
            tmdbItem.copy(isFavorite = isSaved)
        }

    }

    fun getTMDbItemsByCategory(category: Category) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                TMDbPagingSource { page ->
                    fetchTmdbList(category = category, page = page)
                }
            }
        ).flow


    private suspend fun fetchTmdbList(category: Category, page: Int): List<TmdbListItem> {
        if (category.name.trim().isBlank()) {
            return emptyList()
        }
        val response = when (category.categoryType) {
            CategoryType.Movies -> {
                if (category is Category.SearchMovies) {
                    tmdbApi.queryMovies(
                        query = category.name.trim(),
                        page = page,
                        apiKey = TMDB_KEY
                    )
                } else {
                    tmdbApi.getMoviesByCategory(
                        type = category.categoryType.label,
                        category = category.name,
                        page = page,
                        apiKey = TMDB_KEY
                    )
                }
            }
            CategoryType.TVs -> {
                if (category is Category.SearchTVs) {
                    tmdbApi.queryTVs(
                        query = category.name.trim(),
                        page = page,
                        apiKey = TMDB_KEY
                    )
                } else {
                    tmdbApi.getTvsByCategory(
                        type = category.categoryType.label,
                        category = category.name,
                        page = page,
                        apiKey = TMDB_KEY
                    )
                }
            }
        }
        val tmdbResponse = response.body()
        if (response.isSuccessful && tmdbResponse != null) {
            return tmdbResponse.results.map { mapToListItem(it,category.categoryType) }
        } else {
            throw IOException("Failed to catch the list from server")
        }
    }

    fun getCategories(type: CategoryType): List<Category> {
        return when (type) {
            CategoryType.Movies -> MoviesCategories
            CategoryType.TVs -> TVsCategories
        }
    }
}


