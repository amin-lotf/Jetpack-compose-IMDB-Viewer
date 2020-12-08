package com.example.imdbviewer.di

import android.content.Context
import com.example.imdbviewer.data.cache.*
import com.example.imdbviewer.data.network.tmdb.api.TmdbApi
import com.example.imdbviewer.data.network.tmdb.api.TmdbApi.Companion.BASE_URL

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
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
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieRoomDatabase =
        MovieRoomDatabase.getDatabase(context)





    @Singleton
    @Provides
    fun provideMovieDao(movieRoomDatabase: MovieRoomDatabase): MovieDao =
        movieRoomDatabase.movieDao()

    @Singleton
    @Provides
    fun provideTVDao(movieRoomDatabase: MovieRoomDatabase):TVDao =
        movieRoomDatabase.tvDao()

    @Singleton
    @Provides
    fun provideRemoteKeyDao(movieRoomDatabase: MovieRoomDatabase):RemoteKeyDao =
        movieRoomDatabase.remoteKeyDao()

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(@ApplicationContext context: Context):OkHttpClient{
        val cacheSize=10*1024*1024
        val cache=Cache(context.cacheDir,cacheSize.toLong())

        return OkHttpClient.Builder()
            .cache(cache)
//            .addNetworkInterceptor(Interceptor{chain->
//                val original=chain.request()
//
//                val requestBuilder=original.newBuilder()
//                    .header("x-rapidapi-key", RAPID_KEY)
//                    .header("x-rapidapi-host","movies-tvshows-data-imdb.p.rapidapi.com")
//                    .header("useQueryString","true")
//
//                val request=requestBuilder.build()
//                chain.proceed(request)
//            })
            .connectTimeout(100,TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gsonBuilder: Gson,client: OkHttpClient): Retrofit =
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

