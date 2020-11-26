package com.example.imdbviewer.ui.mainscreen

import androidx.paging.PagingData
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CategoryViewState (
    val pagingData: Flow<PagingData<Movie>> = flow {},
    val subCategories: List<Category> = emptyList(),
    val selectedCategory:Category?=null
    )