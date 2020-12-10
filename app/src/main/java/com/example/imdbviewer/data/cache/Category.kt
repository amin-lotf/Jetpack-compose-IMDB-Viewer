package com.example.imdbviewer.data.cache

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class Category(
    val id: Long,
    val name: String,
    val label: String,
    val categoryType: CategoryType
) {

    //Movie
    object ComingSoonMovies :
        Category(3, "upcoming", "Coming Soon", CategoryType.Movies)

    object TopRatedMovies : Category(4, "top_rated", "Top Rated", CategoryType.Movies)
    object PopularMovies : Category(5, "popular", "Popular", CategoryType.Movies)
    object NowPlayingMovies :
        Category(6, "now_playing", "Now Playing", CategoryType.Movies)

    //TV
    object AiringTodayTVs :
        Category(7, "airing_today", "Airing Today", CategoryType.TVs)
    object TopRatedTVs : Category(9, "top_rated", "Top Rated", CategoryType.TVs)
    object PopularTVs : Category(10, "popular", "Popular", CategoryType.TVs)
    object OnAirTVs : Category(11, "on_the_air", "On Air", CategoryType.TVs)

    //Search
    class SearchMovies(query:String):Category(12,name = query,label = query,CategoryType.Movies)
    class SearchTVs(query:String):Category(12,name = query,label = query,CategoryType.TVs)

}

data class ItemType(
    val id:Int=0,
    val type: CategoryType?=null
)


enum class CategoryType(val title: String,val label: String)  {
    Movies(title = "MOVIE",label = "movie"),
    TVs(title = "TV",label = "tv")
}

val MoviesCategories = listOf(
    Category.NowPlayingMovies,
    Category.PopularMovies,
    Category.TopRatedMovies,
    Category.ComingSoonMovies,

)
val TVsCategories = listOf(
    Category.AiringTodayTVs,
    Category.PopularTVs,
    Category.OnAirTVs,
    Category.TopRatedTVs
)


