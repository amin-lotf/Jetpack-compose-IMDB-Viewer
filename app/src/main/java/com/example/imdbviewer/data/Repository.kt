package com.example.imdbviewer.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.imdbviewer.data.cache.*
import com.example.imdbviewer.data.network.ImdbResponse
import com.example.imdbviewer.data.network.RapidPagingSource
import com.example.imdbviewer.data.network.api.ImdbApi
import com.example.imdbviewer.data.network.api.ImdbApi.Companion.MOVIE_DETAILS_TYPE
import com.example.imdbviewer.models.RapidItem
import com.example.imdbviewer.models.RapidMovie
import com.example.imdbviewer.models.RapidMovieDetails
import com.example.imdbviewer.models.RapidTVDetails
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val imdbApi: ImdbApi,
    private val movieDao: MovieDao,
    private val tvDao: TVDao,
    private val movieRoomDatabase: MovieRoomDatabase,
    private val remoteKeyDao: RemoteKeyDao
) {
    val TAG = "aminjoon"

    suspend fun getMovieDetail(imdbId: String): Flow<RapidMovieDetails> = flow {

        val response = imdbApi.getRapidMovieDetails(
            type = MOVIE_DETAILS_TYPE,
            imdbId = imdbId
        )
        if (response.status != null && response.status == "OK") {
            emit(
                response
            )
        }else{
            throw Exception("No network Response")
        }

    }.flowOn(IO)

    suspend fun getTVDetail(imdbId: String): Flow<RapidTVDetails> = flow {

        val response = imdbApi.getRapidTVDetails(
            type = MOVIE_DETAILS_TYPE,
            imdbId = imdbId
        )
        if (response.status != null && response.status == "OK") {
            emit(
                response
            )
        }else{
            throw Exception("No network Response")
        }

    }.flowOn(IO)


    fun getMoviesByCategory(category: NewCategory) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                RapidPagingSource { page ->
                    imdbApi.getRapidMoviesByCategory(type = category.name, page = page)
                }
            }
        ).flow

    fun getTVsByCategory(category: NewCategory) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                RapidPagingSource { page ->
                    imdbApi.getRapidTVsByCategory(type = category.name, page = page)
                }
            }
        ).flow

    fun getCategories(type: CategoryType): List<NewCategory> {
        return when (type) {
            CategoryType.Movies -> MoviesCategories
            CategoryType.TVs -> TVsCategories
        }
    }

}


