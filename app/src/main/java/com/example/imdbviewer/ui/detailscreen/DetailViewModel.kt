package com.example.imdbviewer.ui.detailscreen

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.ItemType
import com.example.imdbviewer.models.tmdb.item.TmdbItemDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    val TAG = "aminjoon"

    private var job: Job? = null

    private val _state = MutableStateFlow(DetailScreenViewState())

    val state: StateFlow<DetailScreenViewState>
        get() = _state


    fun updateFavoriteState(tmdbItemDetails: TmdbItemDetails, isFavorite: Boolean) {
        job?.cancel()
        job = viewModelScope.launch {
            repository.updateTmdbFavoriteState(
                tmdbItemDetails = tmdbItemDetails,
                isFavorite = isFavorite
            )
        }
    }

    fun prepareDetailScreenViewState(tmdbId: Int, type: CategoryType) {
        viewModelScope.launch {
            repository.getTmdbItemDetails(itemId = tmdbId, type = type)
                .catch {
                    //TODO catch error
//                  throw it
                    it.printStackTrace()
                }.collect { itemDetails ->
                    _state.value = DetailScreenViewState(itemDetails)
                }
        }
    }


}
