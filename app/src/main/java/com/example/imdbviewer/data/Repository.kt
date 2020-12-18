package com.example.imdbviewer.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.paging.*
import com.example.imdbviewer.data.cache.*
import com.example.imdbviewer.data.cache.dao.SyncStatDao
import com.example.imdbviewer.data.cache.dao.TmdbDao
import com.example.imdbviewer.data.cache.models.ListItemEntity
import com.example.imdbviewer.data.cache.models.SyncStatEntity
import com.example.imdbviewer.data.network.firebase.FirebaseStorageUtil
import com.example.imdbviewer.data.network.tmdb.TMDbPagingSource
import com.example.imdbviewer.data.network.tmdb.api.TmdbApi
import com.example.imdbviewer.data.network.firebase.FirestoreUtil
import com.example.imdbviewer.data.network.firebase.model.UserDto
import com.example.imdbviewer.data.state.DataState
import com.example.imdbviewer.domain_models.User
import com.example.imdbviewer.domain_models.TmdbItemDetails
import com.example.imdbviewer.domain_models.TmdbListItem
import com.example.imdbviewer.domain_models.UserPreferences
import com.example.imdbviewer.util.PreferencesKeys
import com.example.imdbviewer.util.TMDB_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class Repository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val tmdbDao: TmdbDao,
    private val syncStatDao: SyncStatDao,
    private val mappers: Mappers,
    private val dataStore:DataStore<Preferences>
) {

    val userPreferencesFlow=dataStore.data
        .catch { exception->
            if(exception is IOException){
                emptyPreferences()
            }else{
                throw exception
            }
        }.map { preferences->
            val nightMode=preferences[PreferencesKeys.NIGHT_MODE]?:true
            UserPreferences(inDarkMode = nightMode)
        }

    fun changeDarkMode(inDarkMode:Boolean)= flow {
        emit(DataState.loading<Unit>())

        try {
            dataStore.edit { preferences->
                preferences[PreferencesKeys.NIGHT_MODE]=inDarkMode
            }
            emit(DataState.success(Unit))
        }catch (t:Throwable){
            emit(DataState.failed<Unit>("Failed to change night mode status"))
        }

    }
    

    private suspend fun updateSyncStat() {
        syncStatDao.clearSyncStat()

        syncStatDao.createSyncStat(
            SyncStatEntity(lastSync = System.currentTimeMillis())
        )
    }


    fun getSavedItems() =
        tmdbDao.getFavoritesFlow().map { list ->
            list.map { mappers.listItemMapper.mapToDomainModel(it) }
        }


    fun handleUserLogin() = flow {
        emit(DataState.loading<Unit>())
        try {
            FirestoreUtil.initCurUserIfFirstTime()
            emit(DataState.success(Unit))
        } catch (t: Throwable) {
            emit(DataState.failed<Unit>(t.message ?: "Unknown error while trying to login"))
        }
    }

    suspend fun updateUserInfo(user: User) {
        val photoPath = user.pictureUri?.let {
            FirebaseStorageUtil.uploadProfilePhoto(it)
        }
        FirestoreUtil.updateCurUser(name = user.name, profilePicturePath = photoPath)
    }


    fun getUserInfo(): Flow<DataState<User>> = callbackFlow {

        offer(DataState.loading<User>())

        val subscription = FirestoreUtil.curUserDocRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                offer(DataState.failed<User>("Error Receiving User Info"))
            } else {
                snapshot?.let {
                    if (snapshot.exists()) {
                        val user = snapshot.toObject(UserDto::class.java)
                        user?.let { firebaseUser ->
                            val domainUser = mappers.userMapper.mapToDomainModel(firebaseUser)

                            offer(DataState.success(domainUser))
                        }
                    }
                }
            }
        }
        awaitClose { subscription.remove() }
    }.flatMapLatest {
        getProfilePhotoLink(it)
    }

    private fun getProfilePhotoLink(state: DataState<User>) = flow {
        if (state is DataState.Success) {
            val user = state.data
            val link = withTimeoutOrNull(5000L) {
                FirebaseStorageUtil.getPhotoLink(user.profilePicturePath)
            }
            emit(DataState.success(user.copy(pictureUri = link)))
        } else {
            emit(state)
        }
    }

    suspend fun updateTmdbFavoriteState(tmdbItemDetails: TmdbItemDetails, isFavorite: Boolean) {
        if (isFavorite) {
            val tmdbItemEntity = ListItemEntity(
                id = tmdbItemDetails.id,
                title = tmdbItemDetails.title,
                posterPath = tmdbItemDetails.posterPath,
                voteAverage = tmdbItemDetails.voteAverage,
                year = tmdbItemDetails.releaseYear,
                category = tmdbItemDetails.category,
                isSynced = false,
                timeAdded = System.currentTimeMillis()
            )
            tmdbDao.insertTmdbItem(tmdbItemEntity)
        } else {
            tmdbDao.deleteItem(tmdbItemDetails.id)
        }
    }


    fun getTmdbItemDetails(itemId: Int, type: CategoryType): Flow<TmdbItemDetails?> {
        val tmdbItemFlow = flow {
            val details = when (type) {
                CategoryType.Movies -> getMovieDetails(itemId)
                CategoryType.TVs -> getTvDetails(itemId)
            }
            emit(details)
        }
        return combine(
            tmdbItemFlow,
            tmdbDao.getItem(itemId).map { it != null }
        ) { tmdbItem, isSaved ->
            tmdbItem?.copy(isFavorite = isSaved)
        }

    }

    private suspend fun getTvDetails(itemId: Int): TmdbItemDetails? {
        val response = tmdbApi.getTVDetails(
            tvId = itemId,
            apiKey = TMDB_KEY
        )
        val details = response.body()
        return details?.let {
            mappers.tvDetailsMapper.mapToDomainModel(it)
        }
    }

    private suspend fun getMovieDetails(itemId: Int): TmdbItemDetails? {
        val response = tmdbApi.getMovieDetails(
            movieId = itemId,
            apiKey = TMDB_KEY
        )
        val details = response.body()
        return details?.let {
            mappers.movieDetailsMapper.mapToDomainModel(it)
        }
    }

    fun getTMDbItemsByCategory(category: Category) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                TMDbPagingSource { page ->
                    fetchTmdbList(category = category, page = page)
                }
            }
        ).flow


    private suspend fun fetchTmdbList(category: Category, page: Int): List<TmdbListItem> {
        if (category.name.trim().isBlank()) {
            return emptyList()
        }
        return when (category.categoryType) {
            CategoryType.Movies ->  getTmdbMovieList(category, page)
            CategoryType.TVs -> getTmdbTvList(category, page)
        }
    }

    private suspend fun getTmdbTvList(
        category: Category,
        page: Int
    ): List<TmdbListItem> {
        val response = requestTV(category, page)
        val tmdbResponse = response.body()
        if (response.isSuccessful && tmdbResponse != null) {
            return tmdbResponse.results.map {
                mappers.tvListItemMapper.mapToDomainModel(
                    it
                )
            }
        } else {
            throw IOException("Failed to catch the list from server")
        }
    }

    private suspend fun getTmdbMovieList(
        category: Category,
        page: Int
    ): List<TmdbListItem> {
        val response = requestMovie(category, page)
        val tmdbResponse = response.body()
        if (response.isSuccessful && tmdbResponse != null) {
            return tmdbResponse.results.map {
                mappers.movieListItemMapper.mapToDomainModel(
                    it
                )
            }
        } else {
            throw IOException("Failed to catch the list from server")
        }
    }

    private suspend fun requestTV(
        category: Category,
        page: Int
    ) = if (category is Category.SearchTVs) {
        tmdbApi.queryTVs(
            query = category.name.trim(),
            page = page,
            apiKey = TMDB_KEY
        )
    } else {
        tmdbApi.getTvsByCategory(
            type = category.categoryType.label,
            category = category.name,
            page = page,
            apiKey = TMDB_KEY
        )
    }

    private suspend fun requestMovie(
        category: Category,
        page: Int
    ) = if (category is Category.SearchMovies) {
        tmdbApi.queryMovies(
            query = category.name.trim(),
            page = page,
            apiKey = TMDB_KEY
        )
    } else {
        tmdbApi.getMoviesByCategory(
            type = category.categoryType.label,
            category = category.name,
            page = page,
            apiKey = TMDB_KEY
        )
    }

    fun syncFavoriteList() = flow {

        emit(DataState.Loading<Unit>())

        val lastSyncStat = syncStatDao.getSyncStat()
        val lastSyncTime = lastSyncStat?.lastSync ?: 0


        val cloudData = FirestoreUtil.getSyncedFavorites()
        FirestoreUtil.getSyncedFavorites()
        val localData = tmdbDao.getFavoritesList()

        val cloudDataIds = cloudData.map { it.id }
        val localDataIds = localData.map { it.id }

        val cloudDataIdsToRemove = cloudDataIds.subtract(localDataIds).map { id ->
            cloudData.first { it.id == id }
        }.filter { it.timeSynced!! < lastSyncTime }


        cloudDataIdsToRemove.forEach {
            FirestoreUtil.removeFromFavorites(it.id)
        }

        val localDataToRemove = localDataIds.subtract(cloudDataIds).map { id ->
            localData.first { it.id == id }
        }.filter { it.isSynced }


        tmdbDao.deleteItems(localDataToRemove)

        localData.forEach { entity ->
            if (!entity.isSynced && !cloudDataIds.contains(entity.id)) {
                FirestoreUtil.insertIntoFavorites(
                    mappers.firestoreCacheMapper.mapToDto(entity)
                )
                tmdbDao.updateItem(entity.copy(isSynced = true))
            }
        }

        val cloudDataToAdd = cloudData.filter { dto ->
            dto.timeSynced!! > lastSyncTime && !localDataIds.contains(dto.id)
        }

        tmdbDao.insertTmdbItems(
            cloudDataToAdd.map {
                mappers.firestoreCacheMapper.mapToEntity(it)
            }
        )
        updateSyncStat()
        emit(DataState.success(Unit))
    }

    fun getCategories(type: CategoryType): List<Category> {
        return when (type) {
            CategoryType.Movies -> MoviesCategories
            CategoryType.TVs -> TVsCategories
        }
    }
}


