package com.example.imdbviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val id:String,
    val categoryType:String,
    val prevKey:Int?,
    val nextKey:Int?
)