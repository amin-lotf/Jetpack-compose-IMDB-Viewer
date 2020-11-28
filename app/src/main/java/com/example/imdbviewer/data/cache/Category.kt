package com.example.imdbviewer.data.cache


sealed class NewCategory(
    val id: Long,
    val name: String,
    val label: String,
    val categoryType: CategoryType
) {
    //MOVIE
    object BoxOfficeMovies :
        NewCategory(1, "get-boxoffice-movies", "Box Office", CategoryType.Movies)

    object RecentlyAddedMovies :
        NewCategory(2, "get-recently-added-movies", "Recently Added", CategoryType.Movies)

    object ComingSoonMovies :
        NewCategory(3, "get-upcoming-movies", "Coming Soon", CategoryType.Movies)

    object TrendingMovies : NewCategory(4, "get-trending-movies", "Trending", CategoryType.Movies)
    object NowPlayingMovies :
        NewCategory(5, "get-nowplaying-movies", "Now Playing", CategoryType.Movies)

    //TV
    object AiringTodayTVs :
        NewCategory(6, "get-airingtoday-shows", "Airing Today", CategoryType.TVs)

    object RecentlyAddedTVs :
        NewCategory(7, "get-recently-added-shows", "Recently Added", CategoryType.TVs)

    object TrendingTVs : NewCategory(8, "get-trending-shows", "Trending", CategoryType.TVs)
    object PopularTVs : NewCategory(9, "get-popular-shows", "Popular", CategoryType.TVs)
    object OnAirTVs : NewCategory(10, "get-onair-shows", "On Air", CategoryType.TVs)

}


sealed class CategoryType(val title: String) {
    object Movies : CategoryType(title = "Movie")
    object TVs : CategoryType(title = "TV")
}

val MoviesCategories = listOf(
    NewCategory.TrendingMovies,
    NewCategory.BoxOfficeMovies,
    NewCategory.ComingSoonMovies,
    NewCategory.NowPlayingMovies,
    NewCategory.RecentlyAddedMovies
)
val TVsCategories = listOf(
    NewCategory.AiringTodayTVs,
    NewCategory.OnAirTVs,
    NewCategory.PopularTVs,
    NewCategory.TrendingTVs,
    NewCategory.RecentlyAddedTVs
)


