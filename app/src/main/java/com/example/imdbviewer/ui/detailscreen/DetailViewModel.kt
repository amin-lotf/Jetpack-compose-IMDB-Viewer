package com.example.imdbviewer.ui.detailscreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.domain_models.TmdbItemDetails
import com.example.imdbviewer.domain_models.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DetailViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {


    private var job: Job? = null

    private val _state = MutableStateFlow(DetailScreenViewState())

    val state: StateFlow<DetailScreenViewState>
        get() = _state
    private val _userPreferences = MutableStateFlow(UserPreferences(true))

    val userPreferences: StateFlow<UserPreferences>
        get() = _userPreferences

    init {
        viewModelScope.launch {
            repository.userPreferencesFlow
                .catch {
                    emit(UserPreferences(true))
                }.collect {
                    _userPreferences.value = it
                }
        }

    }

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
