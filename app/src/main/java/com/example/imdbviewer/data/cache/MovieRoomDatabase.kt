package com.example.imdbviewer.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imdbviewer.data.cache.dao.SyncStatDao
import com.example.imdbviewer.data.cache.dao.TmdbDao
import com.example.imdbviewer.data.cache.models.ListItemEntity
import com.example.imdbviewer.data.cache.models.SyncStatEntity


@Database(entities = [ListItemEntity::class,SyncStatEntity::class], version = 10, exportSchema = false)
abstract class MovieRoomDatabase : RoomDatabase() {

        abstract fun tmdbDao(): TmdbDao
        abstract fun syncStatDao():SyncStatDao

    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getDatabase(context: Context): MovieRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "movie_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}