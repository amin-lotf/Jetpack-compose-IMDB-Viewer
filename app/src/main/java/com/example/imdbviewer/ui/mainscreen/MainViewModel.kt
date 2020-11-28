package com.example.imdbviewer.ui.mainscreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.NewCategory
import com.example.imdbviewer.models.RapidMovie
import com.example.imdbviewer.models.RapidTV
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val TAG = "aminjoon"


    private val _selectedMovieCategory:MutableStateFlow<NewCategory> = MutableStateFlow(NewCategory.TrendingMovies)
    private val _selectedTvCategory:MutableStateFlow<NewCategory> = MutableStateFlow(NewCategory.AiringTodayTVs)

    private val _moviesCategories = MutableStateFlow(repository.getCategories(CategoryType.Movies))
    private val _tvsCategories = MutableStateFlow(repository.getCategories(CategoryType.TVs))


    private val _mainScreenState = MutableStateFlow(MainScreenViewState())
    private val _moviesViewState = MutableStateFlow(CategoryViewState<RapidMovie>())
    private val _tvsViewState = MutableStateFlow(CategoryViewState<RapidTV>())


    private val moviesViewState: StateFlow<CategoryViewState<RapidMovie>>
        get() = _moviesViewState

    private val tvsViewState: StateFlow<CategoryViewState<RapidTV>>
        get() = _tvsViewState



    val mainScreenState: StateFlow<MainScreenViewState>
        get() = _mainScreenState


    init {

        viewModelScope.launch {
            combine(
                _moviesCategories,
                _selectedMovieCategory
            ){moviesCategories,selectedCategory->
                CategoryViewState(
                    subCategories = moviesCategories,
                    pagingData = repository.getMoviesByCategory(selectedCategory),
                    selectedCategory = selectedCategory
                )
            }.catch {throwable->
                //TODO : handle error
                throw throwable
            }.collect {
                _moviesViewState.value=it
            }
        }

        viewModelScope.launch {
            combine(
                _tvsCategories,
                _selectedTvCategory
            ){tvsCategories,selectedCategory->
                CategoryViewState(
                    subCategories = tvsCategories,
                    pagingData = repository.getTVsByCategory(selectedCategory),
                    selectedCategory = selectedCategory
                )
            }.catch {throwable->
                //TODO : handle error
                throw throwable
            }.collect {
                _tvsViewState.value=it
            }
        }

        viewModelScope.launch {
            combine(
                moviesViewState,
                tvsViewState
            ){movies,tvs->
                MainScreenViewState(
                    moviesViewState = movies,
                    tvsViewState = tvs
                )
            }.catch { throwable->
                //TODO: handle exception
                throw  throwable
            }.collect {
                _mainScreenState.value=it
            }
        }

    }

    fun changeCategory(category:NewCategory){
        when(category.categoryType){
            CategoryType.Movies -> _selectedMovieCategory.value=category
            CategoryType.TVs -> _selectedTvCategory.value=category
        }
    }


}

