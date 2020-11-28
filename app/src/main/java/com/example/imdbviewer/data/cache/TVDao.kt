package com.example.imdbviewer.data.cache

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.imdbviewer.models.RapidTV

@Dao
interface TVDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRapidTVs(tvs:List<RapidTV>)

    @Query("DELETE from rapid_tvs")
    suspend fun deleteAllRapidTVs()

    @Query("SELECT * FROM rapid_tvs")
    fun getRapidTVsPaging(): DataSource.Factory<Int, RapidTV>
}