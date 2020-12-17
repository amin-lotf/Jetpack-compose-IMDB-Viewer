package com.example.imdbviewer.ui.favorites

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.models.SyncStatEntity
import com.example.imdbviewer.data.state.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class FavoritesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val TAG = "aminjoon"

    private val _favoritesState = MutableStateFlow(FavoriteScreenViewState())
    private val _syncStat = MutableStateFlow<DataState<Unit>?>(null)

    val favoritesViewState: StateFlow<FavoriteScreenViewState>
        get() = _favoritesState

    val synStat: StateFlow<DataState<Unit>?>
        get() = _syncStat

    init {
        viewModelScope.launch {
            repository.getSavedItems()
                .catch {
                    Log.d(TAG, "Error in favoriteViewModel: ")
                    it.printStackTrace()
                }.collect { list ->
                    _favoritesState.value = FavoriteScreenViewState(
                        favorites = list
                    )
                }
        }
    }


    fun onRequestSync(){
        viewModelScope.launch {
            repository.syncFavoriteList()
                .flowOn(Dispatchers.IO)
                .catch {e->
                        Log.d(TAG, "onRequestSync: error")
                        e.printStackTrace()
                        emit(DataState.failed("Failed to sync"))

                }.collect {
                    _syncStat.value=it
                }
        }

    }
}