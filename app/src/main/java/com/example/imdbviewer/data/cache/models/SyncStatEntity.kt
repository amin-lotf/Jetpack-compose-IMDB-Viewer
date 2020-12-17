package com.example.imdbviewer.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_table")
data class SyncStatEntity(

    val lastSync:Long
){
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
}