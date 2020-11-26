package com.example.imdbviewer.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.imdbviewer.data.cache.MovieRoomDatabase
import com.example.imdbviewer.data.network.api.ImdbApi
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RemoteKey
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class ImdbRemoteMediator(
    private val query: String,
    private val service: ImdbApi,
    private val movieDatabase: MovieRoomDatabase
) : RemoteMediator<Int, Movie>() {

    private val TAG="Aminjoon"

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        Log.d(TAG, "load: $loadType")
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey=getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1)?: IMDB_STARTIG_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKey= getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key should not be null")

                remoteKey.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)

            }
            LoadType.APPEND -> {
                val remoteKey=getRemoteKeyForLastItem(state)
                if(remoteKey?.nextKey == null){
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKey.nextKey
            }
        }

        try {
            val apiResponse = service.getMoviesByCategory(query)
            val movies = apiResponse.items

            //Simulating Pagination since the api sends all data at same time without pagination
            val startIndex = ((page - 1) * state.config.pageSize).coerceIn(0..movies.size)
            val endIndex = (startIndex + state.config.pageSize).coerceIn(startIndex..movies.size)
            val curMovies = movies.subList(startIndex, endIndex).onEach { it.category=query }

            val endOfPaginationReached = curMovies.isEmpty()

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.remoteKeyDao().clearRemoteKeys()
                    movieDatabase.movieDao().deleteAll(query)
                }

                val prevKey = if (page == IMDB_STARTIG_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys= curMovies.map {
                    RemoteKey(id=it.id,prevKey = prevKey,nextKey = nextKey)
                }

                movieDatabase.remoteKeyDao().insertAll(keys)
                movieDatabase.movieDao().insetMovies(curMovies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)


        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>):RemoteKey?{
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie->
            movieDatabase.remoteKeyDao().remoteKeysId(movie.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>):RemoteKey?{
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie->
            movieDatabase.remoteKeyDao().remoteKeysId(movie.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ):RemoteKey?{
        return state.anchorPosition?.let { position->
            state.closestItemToPosition(position)?.id?.let { movieId->
                movieDatabase.remoteKeyDao().remoteKeysId(movieId)
            }
        }
    }

    companion object {
        const val IMDB_STARTIG_PAGE = 1
    }
}