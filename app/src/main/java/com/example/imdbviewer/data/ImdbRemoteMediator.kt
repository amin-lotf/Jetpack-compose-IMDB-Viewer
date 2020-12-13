package com.example.imdbviewer.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.MovieRoomDatabase

import com.example.imdbviewer.models.*
import retrofit2.HttpException
import java.io.IOException

//@Suppress("UNCHECKED_CAST")
//@OptIn(ExperimentalPagingApi::class)
//class ImdbRemoteMediator<T:RapidItem>(
//    private val query: String,
//    private val categoryType: CategoryType,
//    private val service: RapidApi,
//    private val movieDatabase: MovieRoomDatabase
//) : RemoteMediator<Int, T>() {
//
//    private val TAG="Aminjoon"
//
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
//        Log.d(TAG, "load: $loadType")
//        val page: Int = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKey=getRemoteKeyClosestToCurrentPosition(state)
//                remoteKey?.nextKey?.minus(1)?: IMDB_STARTING_PAGE
//            }
//            LoadType.PREPEND -> {
//                1
////                val remoteKey= getRemoteKeyForFirstItem(state)
////                    ?: throw InvalidObjectException("Remote key should not be null")
////
////                remoteKey.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
//
//            }
//            LoadType.APPEND -> {
//                1
////                val remoteKey=getRemoteKeyForLastItem(state)
////                if(remoteKey?.nextKey == null){
////                    throw InvalidObjectException("Remote key should not be null for $loadType")
////                }
////                remoteKey.nextKey
//            }
//        }
//
//        try {
//            val apiResponse = when(categoryType){
//                CategoryType.Movies -> service.getRapidMoviesByCategory(query,page = page)
//                CategoryType.TVs -> service.getRapidTVsByCategory(query,page = page)
//            }
//
//            Log.d(
//                TAG,
//                "load: status: ${apiResponse.status} message: ${apiResponse.statusMessage} Items: ${apiResponse.results}"
//            )
//
//            val endOfPaginationReached = apiResponse.items?.isEmpty()?:false
//
//            movieDatabase.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    movieDatabase.remoteKeyDao().clearRemoteKeys(categoryType = categoryType.title)
//                    when(categoryType){
//                        CategoryType.Movies -> movieDatabase.movieDao().deleteAllRapidMovies()
//                        CategoryType.TVs -> movieDatabase.tvDao().deleteAllRapidTVs()
//                        else-> throw IllegalArgumentException("${categoryType.title}  does not exist in database")
//                    }
//                }
//
//                val prevKey = if (page == IMDB_STARTING_PAGE) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//
//                val keys= apiResponse.items?.map {
//                    RemoteKey(id=it.imdbId,prevKey = prevKey,nextKey = nextKey,categoryType = categoryType.title)
//                }
//                keys?.let {
//                    movieDatabase.remoteKeyDao().insertAll(keys)
//                }
//
//                when(categoryType){
//                    CategoryType.Movies -> movieDatabase.movieDao().insertRapidMovies(apiResponse.items as List<RapidMovie>)
//                    CategoryType.TVs -> movieDatabase.tvDao().insertRapidTVs(apiResponse.items as List<RapidTV>)
//                    else->throw IllegalArgumentException("${categoryType.title}  does not exist in database")
//                }
//            }
//            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//
//
//        } catch (exception: IOException) {
//            Log.d(TAG, "load: error: IO")
//            exception.printStackTrace()
//            return MediatorResult.Error(exception)
//        } catch (exception: HttpException) {
//            Log.d(TAG, "load: error: Http")
//            exception.printStackTrace()
//            return MediatorResult.Error(exception)
//        }
//
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, T>):RemoteKey?{
//        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie->
//            movieDatabase.remoteKeyDao().remoteKeysId(movie.imdbId)
//        }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, T>):RemoteKey?{
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie->
//            movieDatabase.remoteKeyDao().remoteKeysId(movie.imdbId)
//        }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(
//        state: PagingState<Int, T>
//    ):RemoteKey?{
//        return state.anchorPosition?.let { position->
//            state.closestItemToPosition(position)?.imdbId?.let { movieId->
//                movieDatabase.remoteKeyDao().remoteKeysId(movieId)
//            }
//        }
//    }
//
//    companion object {
//        const val IMDB_STARTING_PAGE = 1
//    }
//}