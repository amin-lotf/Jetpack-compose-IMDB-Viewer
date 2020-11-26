package com.example.imdbviewer.ui.mainscreen

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.data.cache.CategoryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val TAG = "aminjoon"


    private val _selectedMovieCategory:MutableStateFlow<Category> = MutableStateFlow(Category.Top250Movies)
    private val _selectedTvCategory:MutableStateFlow<Category> = MutableStateFlow(Category.Top250TVs)
    private val _selectedBoxOfficeCategory:MutableStateFlow<Category> = MutableStateFlow(Category.BoxOffice)

    private val _currentMoviesPaging =
        MutableStateFlow(repository.getMoviesByCategory(_selectedMovieCategory.value))

    private val _currentTvsPaging =
        MutableStateFlow(repository.getMoviesByCategory(_selectedTvCategory.value))

    private val _currentBoxOfficePaging =
        MutableStateFlow(repository.getMoviesByCategory(_selectedBoxOfficeCategory.value))

    private val _moviesCategories = MutableStateFlow(repository.getCategories(CategoryType.Movies))
    private val _tvsCategories = MutableStateFlow(repository.getCategories(CategoryType.TVs))
    private val _boxOfficeCategories = MutableStateFlow(repository.getCategories(CategoryType.BoxOffice))

    private val _mainScreenState = MutableStateFlow(MainScreenViewState())
    private val _moviesViewState = MutableStateFlow(CategoryViewState())
    private val _tvsViewState = MutableStateFlow(CategoryViewState())
    private val _boxOfficeViewState = MutableStateFlow(CategoryViewState())

    private val moviesViewState: StateFlow<CategoryViewState>
        get() = _moviesViewState

    private val tvsViewState: StateFlow<CategoryViewState>
        get() = _tvsViewState

    private val boxOfficeViewState: StateFlow<CategoryViewState>
        get() = _boxOfficeViewState

    val mainScreenState: StateFlow<MainScreenViewState>
        get() = _mainScreenState


    init {
        viewModelScope.launch {
            combine(
                _moviesCategories,
                _currentMoviesPaging,
                _selectedMovieCategory
            ){moviesCategories,moviesPaging,selectedCategory->
                requestNewMovies(CategoryType.Movies)
                CategoryViewState(
                    subCategories = moviesCategories,
                    pagingData = repository.getMoviesByCategory(_selectedMovieCategory.value),
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
                _currentTvsPaging,
                _selectedTvCategory
            ){tvsCategories,tvsPaging,selectedCategory->
                requestNewMovies(CategoryType.TVs)
                CategoryViewState(
                    subCategories = tvsCategories,
                    pagingData = tvsPaging,
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
                _boxOfficeCategories,
               _currentBoxOfficePaging,
                _selectedBoxOfficeCategory
            ){boxOfficeCategories,boxOfficePaging,selectedCategory->
                requestNewMovies(CategoryType.BoxOffice)
                 CategoryViewState(
                    subCategories = boxOfficeCategories,
                    pagingData = boxOfficePaging,
                    selectedCategory = selectedCategory
                )
            }.catch {throwable->
                //TODO : handle error
                throw throwable
            }.collect {
                _boxOfficeViewState.value=it
            }
        }

        viewModelScope.launch {
            combine(
                moviesViewState,
                tvsViewState,
                boxOfficeViewState
            ){movies,tvs,boxOffice->
                MainScreenViewState(
                    moviesViewState = movies,
                    tvsViewState = tvs,
                    boxOfficeViewState = boxOffice
                )
            }.catch { throwable->
                //TODO: handle exception
                throw  throwable
            }.collect {
                _mainScreenState.value=it
            }
        }

    }




    fun requestNewMovies(type: CategoryType) {
        Log.d(TAG, "requestNewMovies: called")
        val forCategory = when (type) {
            CategoryType.Movies -> _selectedMovieCategory
            CategoryType.TVs -> _selectedTvCategory
            CategoryType.BoxOffice -> _selectedBoxOfficeCategory
        }

        viewModelScope.launch {
            repository.getCurrentMovies(forCategory.value)
        }
    }

    fun changeCategory(category:Category){
        when(category.categoryType){
            CategoryType.Movies -> _selectedMovieCategory.value=category
            CategoryType.TVs -> _selectedTvCategory.value=category
            CategoryType.BoxOffice -> _selectedBoxOfficeCategory.value=category
        }
    }


}

