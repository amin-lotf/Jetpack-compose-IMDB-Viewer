package com.example.imdbviewer.data.cache

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidMovie


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRapidMovies(movies:List<RapidMovie>)

    @Query("DELETE from rapid_movies")
    suspend fun deleteAllRapidMovies()

    @Query("SELECT * FROM rapid_movies")
    fun getRapidMoviesPaging():DataSource.Factory<Int, RapidMovie>

    @Query("SELECT * FROM movie_table where category=:category")
    fun getMoviesPaging(category:String):DataSource.Factory<Int, Movie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetMovies(movies:List<Movie>)

    @Query("DELETE from movie_table where category=:category")
    suspend fun deleteAll(category:String)
}