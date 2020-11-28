package com.example.imdbviewer.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidMovie
import com.example.imdbviewer.models.RapidTV
import com.example.imdbviewer.models.RemoteKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@Database(entities = [Movie::class,RemoteKey::class,RapidMovie::class,RapidTV::class],version = 5, exportSchema = false)
abstract class MovieRoomDatabase:RoomDatabase() {
    abstract fun movieDao():MovieDao
    abstract fun tvDao():TVDao
    abstract fun remoteKeyDao():RemoteKeyDao

    companion object{
        @Volatile
        private var INSTANCE:MovieRoomDatabase?=null

        private class MovieDatabaseCallback():RoomDatabase.Callback(){

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let {database->
                    CoroutineScope(IO).launch {
      //                  val movieDao=database.movieDao()

//                        movieDao.deleteAll()
//
//                        movieDao.insetMovies(
//                            listOf(
//                                Movie("1","A movie","A category",2000),
//                                Movie("2","B movie","A category",2001),
//                                Movie("3","C movie","A category",2002),
//                                Movie("4","D movie","A category",2003),
//
//                            )
//                        )

                    }
                }
            }
        }

        fun getDatabase(context: Context):MovieRoomDatabase{
            return INSTANCE?: synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                "movie_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(MovieDatabaseCallback())
                    .build()
                INSTANCE=instance
                instance
            }
        }

    }

}