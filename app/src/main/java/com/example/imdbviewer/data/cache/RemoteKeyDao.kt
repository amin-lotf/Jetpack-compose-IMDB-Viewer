package com.example.imdbviewer.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imdbviewer.models.RemoteKey

@Dao
interface RemoteKeyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey:List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id=:id")
    suspend fun remoteKeysId(id:String):RemoteKey?


    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}