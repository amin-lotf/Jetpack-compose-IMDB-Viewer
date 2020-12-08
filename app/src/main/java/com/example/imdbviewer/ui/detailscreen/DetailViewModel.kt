package com.example.imdbviewer.ui.detailscreen

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbviewer.data.Repository
import com.example.imdbviewer.data.cache.CategoryType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
private val repository: Repository
):ViewModel() {
    val TAG="aminjoon"

    private val _state= MutableStateFlow( DetailScreenViewState())


   val state:StateFlow<DetailScreenViewState>
   get() = _state


   fun prepareDetailScreenViewState(tmdbId:Int, type:CategoryType){
       Log.d(TAG, "prepareDetailScreenViewState: id: $tmdbId ")
       viewModelScope.launch {
          repository.getTmdbItemDetails(itemId = tmdbId,type = type)
              .catch {
                  //TODO catch error
//                  throw it
                  it.printStackTrace()
              }.collect {itemDetails->
                  _state.value=DetailScreenViewState(itemDetails)
              }
       }
   }
      


}