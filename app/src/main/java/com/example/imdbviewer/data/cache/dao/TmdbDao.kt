package com.example.imdbviewer.data.cache.dao

import androidx.room.*
import com.example.imdbviewer.data.cache.models.ListItemEntity
import com.example.imdbviewer.domain_models.TmdbListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TmdbDao {

    @Insert
    suspend fun insertTmdbItem(item: ListItemEntity)

    @Insert
    suspend fun insertTmdbItems(items:List<ListItemEntity>)

    @Query("SELECT * FROM tmdb_items WHERE id=:itemId")
    fun getItem(itemId:Int):Flow<ListItemEntity?>

    @Update
    fun updateItem(item: ListItemEntity)

    @Query("SELECT * FROM tmdb_items")
    fun getFavoritesFlow():Flow<List<ListItemEntity>>

    @Query("SELECT * FROM tmdb_items")
    fun getFavoritesList():List<ListItemEntity>

    @Query("DELETE FROM tmdb_items WHERE id=:itemId")
    suspend fun deleteItem(itemId: Int)

    @Delete
    suspend fun deleteItems(items:List<ListItemEntity>)

}