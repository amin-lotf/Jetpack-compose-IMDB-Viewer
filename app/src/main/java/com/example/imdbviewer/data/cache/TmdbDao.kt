package com.example.imdbviewer.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.imdbviewer.models.tmdb.item.TmdbItemDetails
import com.example.imdbviewer.models.tmdb.item.TmdbListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TmdbDao {

    @Insert
    suspend fun insertTmdbItem(item:TmdbListItem)

    @Query("SELECT * FROM tmdb_items WHERE id=:itemId")
    fun getItem(itemId:Int):Flow<TmdbListItem?>

    @Query("SELECT * FROM tmdb_items")
    fun getFavorites():Flow<List<TmdbListItem>>

    @Query("DELETE FROM tmdb_items WHERE id=:itemId")
    suspend fun deleteItem(itemId: Int)

}