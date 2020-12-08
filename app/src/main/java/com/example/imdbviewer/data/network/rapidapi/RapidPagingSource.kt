package com.example.imdbviewer.data.network.rapidapi

import androidx.paging.PagingSource
import com.example.imdbviewer.data.network.rapidapi.api.RapidResponse
import com.example.imdbviewer.models.RapidItem
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RapidPagingSource<T:RapidResponse>(
    private val query: suspend  (page: Int) -> Response<T>
) : PagingSource<Int, RapidItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RapidItem> {
        val page = params.key ?: IMDB_STARTING_PAGE

        return try {
            val response = query(page)
            val rapidResponse=response.body()
            if (response.isSuccessful && rapidResponse!=null){
                if ( rapidResponse.status == null || rapidResponse.status != "OK") {
                    throw IOException("Failed to get data from the server")
                }
                else{
                    val items = rapidResponse.items ?: emptyList()
                    LoadResult.Page(
                        data = items,
                        prevKey = if (page == IMDB_STARTING_PAGE) null else page - 1,
                        nextKey = if (items.isEmpty()) null else page + 1
                    )
                }

            }else{
                throw IOException(response.errorBody().toString())
            }


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