package com.example.imdbviewer.data.network.tmdb

import android.util.Log
import androidx.paging.PagingSource
import com.example.imdbviewer.domain_models.TmdbListItem
import retrofit2.HttpException
import java.io.IOException

class TMDbPagingSource(
    private val query: suspend (page: Int) -> List<TmdbListItem>
) : PagingSource<Int, TmdbListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TmdbListItem> {
        val page = params.key ?: TMDB_STARTING_PAGE
        return try {
            val items = query(page)
            LoadResult.Page(
                data = items,
                prevKey = if (page == TMDB_STARTING_PAGE) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            exception.printStackTrace()
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            exception.printStackTrace()
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val TMDB_STARTING_PAGE = 1
    }
}