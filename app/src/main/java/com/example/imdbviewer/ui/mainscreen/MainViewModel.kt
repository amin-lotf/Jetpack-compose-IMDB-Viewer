package com.example.imdbviewer.ui.mainscreen

import android.net.Uri
import android.util.Log
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.data.cache.Category
import com.example.imdbviewer.firebase.FirebaseAuthUtil
import com.example.imdbviewer.firebase.FirebaseStorageUtil
import com.example.imdbviewer.firebase.FirestoreUtil
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseUiException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.NullPointerException

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    private val TAG = "aminjoon"

    private var _lastSelectedMovieCategory: Category = Category.NowPlayingMovies
    private var _lastSelectedTvCategory: Category = Category.AiringTodayTVs


    private val _userSate = MutableStateFlow(UserState())

    private val _selectedMovieCategory: MutableStateFlow<Category> =
        MutableStateFlow(_lastSelectedMovieCategory)
    private val _selectedTvCategory: MutableStateFlow<Category> =
        MutableStateFlow(_lastSelectedTvCategory)

    private val _inSearchMode = MutableStateFlow(false)
    private val _searchQuery = MutableStateFlow("")

    private val _moviesSubCategories =
        MutableStateFlow(repository.getCategories(CategoryType.Movies))
    private val _tvsSubCategories = MutableStateFlow(repository.getCategories(CategoryType.TVs))

    private val _mainScreenState = MutableStateFlow(MainScreenViewState())

    val mainScreenState: StateFlow<MainScreenViewState>
        get() = _mainScreenState

    val inSearchMode: StateFlow<Boolean>
        get() = _inSearchMode

    init {
        getUserInfo()
        viewModelScope.launch {
            combine(
                getSectionViewState(CategoryType.Movies),
                getSectionViewState(CategoryType.TVs),
                getSearchSectionViewState(),
                _userSate
            ) { movies, tvs, (searchMode, query), userState ->
                MainScreenViewState(
                    moviesViewState = movies,
                    tvsViewState = tvs,
                    userState = userState,
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
                .map { query -> query.trim() }
                .distinctUntilChanged()
                .collect { query ->
                    if (_inSearchMode.value) {
                        _selectedMovieCategory.value = Category.SearchMovies(query)
                        _selectedTvCategory.value = Category.SearchTVs(query)
                    }
                }
        }
    }

    fun switchToSearchMode(inSearchMode: Boolean) {
        _inSearchMode.value = inSearchMode
        if (inSearchMode) {
            _lastSelectedMovieCategory = _selectedMovieCategory.value
            _lastSelectedTvCategory = _selectedTvCategory.value
            _selectedMovieCategory.value = Category.SearchMovies("")
            _selectedTvCategory.value = Category.SearchTVs("")
        } else {
            _searchQuery.value = ""
            _selectedMovieCategory.value = _lastSelectedMovieCategory
            _selectedTvCategory.value = _lastSelectedTvCategory
        }
    }

    fun performSearch(query: String) {
        _searchQuery.value = query
    }

    private fun getSearchSectionViewState() =
        combine(
            _inSearchMode,
            _searchQuery
        ) { inSearchMode, searchQuery ->
            Pair(inSearchMode, searchQuery)
        }

    private fun getSelectedCategory(categoryType: CategoryType) =
        when (categoryType) {
            CategoryType.Movies -> _selectedMovieCategory
            CategoryType.TVs -> _selectedTvCategory
        }

    private fun getSubCategories(categoryType: CategoryType) =
        when (categoryType) {
            CategoryType.Movies -> _moviesSubCategories
            CategoryType.TVs -> _tvsSubCategories
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


    fun changeCategory(category: Category) {
        when (category.categoryType) {
            CategoryType.Movies -> {
                _lastSelectedMovieCategory = _selectedMovieCategory.value
                _selectedMovieCategory.value = category
            }
            CategoryType.TVs -> {
                _selectedTvCategory.value = category
            }
        }
    }

    fun handleFirebaseError(error: FirebaseUiException?) {
        when (error?.errorCode) {
            ErrorCodes.NO_NETWORK -> {
                Log.d(TAG, "handleFirebaseError: net ${error.message}")
            }
            ErrorCodes.UNKNOWN_ERROR -> {
                Log.d(TAG, "handleFirebaseError unk: ${error.message}")
            }
            else -> {
                Log.d(TAG, "handleFirebaseError else: ${error?.message}")
            }
        }
    }

    fun handleUserLogin() {
        viewModelScope.launch {
            FirestoreUtil.initCurUserIfFirstTime { throwable ->
                if (throwable == null) {
                    getUserInfo()
                } else {
                    //TODO handle error
                    throwable.printStackTrace()
                }
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            FirestoreUtil.getCurrentUser { user, throwable ->
                if (throwable == null) {
                    user?.let {
                        _userSate.value = UserState(
                            isSignedIn = true,
                            user = it
                        )
                    }
                } else {
                    _userSate.value = UserState(isSignedIn = false, user = null)
                    //TODO handle error
                }
            }
        }
    }

    fun signOutUser() {
        _userSate.value = UserState(isSignedIn = false, user = null)
        FirebaseAuthUtil.signOut()
    }

    fun onUserStateChange(userState: UserState) {
        when (userState.inEditMode) {
            true -> {
                _userSate.value=userState
            }
            false -> {
                if (userState.shouldSave) {
                    val user = _userSate.value.user
                    user?.let {
                        it.profilePicturePath?.let { photoUri ->
                            FirebaseStorageUtil.uploadProfilePhoto(photoUri as Uri) { downloadLink, throwable ->
                                if (throwable != null || downloadLink == null) {
                                    //TODO handle error
                                    throw throwable
                                        ?: NullPointerException("Failed to upload photo")
                                } else {
                                    updateUserInfo(name = "", imageUri = downloadLink)
                                }
                            }
                        }
                        updateUserInfo(name = user.name, imageUri = null)
                    }

                }
                getUserInfo()
            }
        }
    }

    private fun updateUserInfo(name:String="",imageUri:Any?=null){
        viewModelScope.launch {
            FirestoreUtil.updateCurUser(name = name,profilePicturePath = imageUri){throwable ->
                throwable?.let {
                    //Handle Error
                    throw throwable
                }
                //getUserInfo()
            }
        }
    }

    fun bufferPhoto(uri: Uri){
        val tmpUser=_userSate.value.user?.copy(profilePicturePath = uri)
        _userSate.value=_userSate.value.copy(user = tmpUser)
    }
}

