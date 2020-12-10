package com.example.imdbviewer.ui.mainscreen

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.firebase.FirebaseUtil
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseUiException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    private val TAG = "aminjoon"

    private var _lastSelectedMovieCategory:Category=Category.NowPlayingMovies
    private var _lastSelectedTvCategory:Category=Category.AiringTodayTVs



    private val _userSignedIn= MutableStateFlow(FirebaseUtil.isUserSignedIn)

    private val _selectedMovieCategory: MutableStateFlow<Category> =
        MutableStateFlow(_lastSelectedMovieCategory)
    private val _selectedTvCategory: MutableStateFlow<Category> =
        MutableStateFlow(_lastSelectedTvCategory)

    private val _inSearchMode= MutableStateFlow(false)
    private val _searchQuery= MutableStateFlow("")

    private val _moviesSubCategories = MutableStateFlow(repository.getCategories(CategoryType.Movies))
    private val _tvsSubCategories = MutableStateFlow(repository.getCategories(CategoryType.TVs))

    private val _mainScreenState = MutableStateFlow(MainScreenViewState())

    val mainScreenState: StateFlow<MainScreenViewState>
        get() = _mainScreenState

    val inSearchMode:StateFlow<Boolean>
    get() = _inSearchMode

    init {
        viewModelScope.launch {
            combine(
                getSectionViewState(CategoryType.Movies),
                getSectionViewState(CategoryType.TVs),
                _inSearchMode,
                _searchQuery
            ) { movies, tvs,searchMode,query ->
                MainScreenViewState(
                    moviesViewState = movies,
                    tvsViewState = tvs,
                    inSearchMode = searchMode,
                    searchQuery = query
                )
            }.catch { throwable ->
                //TODO: handle exception
                throw  throwable
            }.collect {
                _mainScreenState.value = it
            }
        }

        viewModelScope.launch {
            _searchQuery.debounce(1000)
                .map { query->query.trim() }
                .distinctUntilChanged()
                .collect { query->
                    if(_inSearchMode.value) {
                        _selectedMovieCategory.value = Category.SearchMovies(query)
                        _selectedTvCategory.value = Category.SearchTVs(query)
                    }
                }
        }
    }

    fun switchToSearchMode(inSearchMode:Boolean){
        _inSearchMode.value=inSearchMode
        if (inSearchMode){
            _lastSelectedMovieCategory=_selectedMovieCategory.value
            _lastSelectedTvCategory=_selectedTvCategory.value
            _selectedMovieCategory.value=Category.SearchMovies("")
            _selectedTvCategory.value=Category.SearchTVs("")
        }else{
            _searchQuery.value=""
            _selectedMovieCategory.value=_lastSelectedMovieCategory
            _selectedTvCategory.value=_lastSelectedTvCategory
        }
    }

    fun performSearch(query:String){
        _searchQuery.value=query
    }

    private fun getSelectedCategory(categoryType: CategoryType)=
        when(categoryType){
            CategoryType.Movies-> _selectedMovieCategory
            CategoryType.TVs->_selectedTvCategory
        }

    private fun getSubCategories(categoryType: CategoryType)=
        when(categoryType){
            CategoryType.Movies-> _moviesSubCategories
            CategoryType.TVs->_tvsSubCategories
        }

     private fun getSectionViewState(categoryType: CategoryType) =
        combine(
            getSubCategories(categoryType),
            getSelectedCategory(categoryType)
        ) { subCategories, selectedCategory ->

            CategoryViewState(
                subCategories = subCategories,
                pagingData = repository.getTMDbItemsByCategory(selectedCategory),
                selectedCategory = selectedCategory
            )
        }.catch { throwable ->
            //TODO : handle error
            throw throwable
        }

//    private fun getSectionsViewState(categoryType: CategoryType) =
//        combine(
//            getSelectedCategory(CategoryType.TVs),
//            getSelectedCategory(CategoryType.Movies)
//        ) { selectedTVCategory, selectedMovieCategory ->
//
//            val movieViewState=CategoryViewState(
//                subCategories = getSubCategories(CategoryType.Movies),
//                pagingData = repository.getTMDbItemsByCategory(selectedCategory),
//                selectedCategory = selectedCategory
//            )
//        }.catch { throwable ->
//            //TODO : handle error
//            throw throwable
//        }



    fun changeCategory(category: Category) {
        when (category.categoryType) {
            CategoryType.Movies -> {
                _lastSelectedMovieCategory=_selectedMovieCategory.value
                _selectedMovieCategory.value = category
            }
            CategoryType.TVs -> {
                _selectedTvCategory.value = category
            }
        }
    }

    fun handleFirebaseError(error: FirebaseUiException?) {
        when(error?.errorCode) {
            ErrorCodes.NO_NETWORK -> {
                Log.d(TAG, "handleFirebaseError: net ${error.message}")
            }
            ErrorCodes.UNKNOWN_ERROR -> {
                Log.d(TAG, "handleFirebaseError unk: ${error.message}")
            }
            else->{Log.d(TAG, "handleFirebaseError else: ${error?.message}")}
        }
    }

    fun updateUserStatus(){
        _userSignedIn.value=FirebaseUtil.isUserSignedIn
    }


}

