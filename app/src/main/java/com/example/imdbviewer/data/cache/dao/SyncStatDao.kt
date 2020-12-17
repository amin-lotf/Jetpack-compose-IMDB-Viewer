package com.example.imdbviewer.data.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.imdbviewer.data.cache.models.SyncStatEntity

@Dao
interface SyncStatDao {

    @Insert
    suspend fun createSyncStat(stat:SyncStatEntity)

    @Query("SELECT * FROM sync_table LIMIT 1")
    suspend fun getSyncStat():SyncStatEntity?

    @Query("DELETE FROM sync_table")
    suspend fun  clearSyncStat()

}