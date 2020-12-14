package com.example.imdbviewer.ui.mainscreen

data class MainScreenViewState(
    val moviesViewState: CategoryViewState =CategoryViewState(),
    val tvsViewState: CategoryViewState =CategoryViewState(),
    val inSearchMode:Boolean=false,
    val searchQuery:String=""
) {
}