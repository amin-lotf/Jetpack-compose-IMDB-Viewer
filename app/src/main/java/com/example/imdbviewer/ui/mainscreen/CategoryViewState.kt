package com.example.imdbviewer.ui.mainscreen

import androidx.paging.PagingData
import com.example.imdbviewer.data.cache.NewCategory
import com.example.imdbviewer.models.Movie
import com.example.imdbviewer.models.RapidItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CategoryViewState<T:RapidItem> (
    val pagingData: Flow< PagingData<T>> = flow {},
    val subCategories: List<NewCategory> = emptyList(),
    val selectedCategory:NewCategory?=null
    )