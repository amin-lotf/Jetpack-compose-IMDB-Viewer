package com.example.imdbviewer.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.createDataStore
import com.example.imdbviewer.R
import com.example.imdbviewer.data.cache.*
import com.example.imdbviewer.data.network.tmdb.api.TmdbApi
import com.example.imdbviewer.data.network.tmdb.api.TmdbApi.Companion.BASE_URL
import com.firebase.ui.auth.AuthUI

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context:Context)=
        context.createDataStore(name = "preferences")

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieRoomDatabase =
        MovieRoomDatabase.getDatabase(context)


    @Singleton
    @Provides
    fun provideTmdbDao(movieRoomDatabase: MovieRoomDatabase) =
        movieRoomDatabase.tmdbDao()

    @Singleton
    @Provides
    fun provideSyncStatDao(movieRoomDatabase: MovieRoomDatabase)=
        movieRoomDatabase.syncStatDao()


    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }


    @Provides
    fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gsonBuilder: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()

    @Singleton
    @Provides
    fun provideIMDBApi(retrofit: Retrofit): TmdbApi =
        retrofit.create(TmdbApi::class.java)
}

