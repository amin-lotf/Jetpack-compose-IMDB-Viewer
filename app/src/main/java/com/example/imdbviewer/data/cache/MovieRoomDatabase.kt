package com.example.imdbviewer.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imdbviewer.models.RemoteKey


@Database(entities = [RemoteKey::class],version = 6, exportSchema = false)
abstract class MovieRoomDatabase:RoomDatabase() {
    abstract fun movieDao():MovieDao
    abstract fun tvDao():TVDao
    abstract fun remoteKeyDao():RemoteKeyDao


    companion object{
        @Volatile
        private var INSTANCE:MovieRoomDatabase?=null

        fun getDatabase(context: Context):MovieRoomDatabase{
            return INSTANCE?: synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                "movie_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                instance
            }
        }

    }

}