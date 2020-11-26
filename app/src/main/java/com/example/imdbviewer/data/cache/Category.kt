package com.example.imdbviewer.data.cache




sealed class Category(val id:Long, val name:String, val label:String,val categoryType: CategoryType){
    object Top250Movies: Category(1,"Top250Movies","Top 250",CategoryType.Movies)
    object InTheaters: Category(2,"InTheaters","In Theaters",CategoryType.Movies)
    object ComingSoon: Category(3,"ComingSoon","Coming Soon",CategoryType.Movies)
    object Top250TVs: Category(4,"Top250TVs","Top 250",CategoryType.TVs)
    object MostPopularTVs: Category(5,"MostPopularTVs","Most Popular",CategoryType.TVs)
    object BoxOffice: Category(6,"BoxOffice","Current",CategoryType.BoxOffice)
   // object BoxOfficeAllTime: Category(7,"BoxOfficeAllTime","All Time",CategoryType.BoxOffice)
}

sealed class CategoryType(val title:String){
    object Movies:CategoryType(title = "Movie")
    object TVs:CategoryType(title = "TV")
    object BoxOffice:CategoryType(title = "Box Office")

}

val MoviesCategories= listOf(Category.Top250Movies,Category.ComingSoon,Category.InTheaters)
val TVsCategories=listOf(Category.Top250TVs,Category.MostPopularTVs)
val BoxOfficeCategories=listOf<Category>()

