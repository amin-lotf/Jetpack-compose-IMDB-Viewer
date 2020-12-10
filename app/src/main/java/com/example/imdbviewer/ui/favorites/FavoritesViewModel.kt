package com.example.imdbviewer.ui.favorites

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesViewModel @ViewModelInject constructor(
    private val repository: Repository
):ViewModel() {

    private val TAG="aminjoon"

    private val _favoritesState= MutableStateFlow(FavoriteScreenViewState())

    val favoritesViewState:StateFlow<FavoriteScreenViewState>
    get() = _favoritesState


    init {
        viewModelScope.launch {
            repository.getSavedItems()
                .catch {
                    Log.d(TAG, "Error in favoriteViewModel: ")
                    it.printStackTrace()
                }.collect { list->
                    _favoritesState.value=FavoriteScreenViewState(
                        favorites = list
                    )
                }
        }
    }


}