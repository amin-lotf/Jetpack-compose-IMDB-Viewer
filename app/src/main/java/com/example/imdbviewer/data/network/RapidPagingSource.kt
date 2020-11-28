package com.example.imdbviewer.data.network

import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.example.imdbviewer.data.cache.MovieRoomDatabase
import com.example.imdbviewer.data.network.api.ImdbApi
import com.example.imdbviewer.data.network.api.RapidResponse
import com.example.imdbviewer.models.RapidItem
import retrofit2.HttpException
import java.io.IOException

class RapidPagingSource<T : RapidItem>(
    private val query: suspend (page: Int) -> RapidResponse<T>
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: IMDB_STARTING_PAGE

        return try {
            val response = query(page)
            val items = response.items ?: emptyList()

            if (response.status == null || response.status != "OK") {
                throw IOException("Failed to get data from the server")
            }
            LoadResult.Page(
                data = items,
                prevKey = if (page == IMDB_STARTING_PAGE) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val IMDB_STARTING_PAGE = 1
    }
}