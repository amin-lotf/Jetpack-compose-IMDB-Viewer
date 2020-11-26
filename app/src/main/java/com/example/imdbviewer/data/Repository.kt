package com.example.imdbviewer.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.imdbviewer.data.cache.*
import com.example.imdbviewer.data.network.ImdbResponse
import com.example.imdbviewer.data.network.api.ImdbApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val imdbApi: ImdbApi,
    private val movieDao: MovieDao,
    private val movieRoomDatabase: MovieRoomDatabase,
    private val remoteKeyDao: RemoteKeyDao
) {
    val TAG = "aminjoon"

    suspend fun getCurrentMovies(category:Category) {
        try {
            Log.d(TAG, "getCurrentMovies: is called")
            val response = imdbApi.getMoviesByCategory(category.name)
            //val response=ImdbResponse(emptyList(),"")
            if (response.errorMessage.isNotBlank()) {
                Log.d(TAG, "getCurrentMovies: error message ${response.errorMessage}")
            } else {
                val totalMovies = response.items
                movieRoomDatabase.withTransaction {
                    movieDao.deleteAll(category.name)
                    Log.d(TAG, "getCurrentMovies: size: ${totalMovies.size}")
                    movieDao.insetMovies(totalMovies.onEach { it.category=category.name })
                }


            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        } catch (exception: HttpException) {
            exception.printStackTrace()
        }

    }

    fun getMoviesByCategory(category: Category)=
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = movieDao.getMoviesPaging(category.name).asPagingSourceFactory()
        ).flow

    fun getCategories(type:CategoryType):List<Category>{
        return when(type){
            CategoryType.Movies -> MoviesCategories
            CategoryType.TVs -> TVsCategories
            CategoryType.BoxOffice -> BoxOfficeCategories
        }
    }

}


