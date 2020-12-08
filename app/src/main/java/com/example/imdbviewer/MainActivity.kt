package com.example.imdbviewer

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.setContent

import com.example.imdbviewer.theme.IMDBViewerTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.ViewModelStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.ui.detailscreen.DetailScreen
import com.example.imdbviewer.ui.detailscreen.DetailsActivity
import com.example.imdbviewer.ui.mainscreen.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalFocus
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG="aminjoon"
    val mainViewModel by viewModels<MainViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMDBViewerTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme

                Surface(color = MaterialTheme.colors.background,modifier = Modifier.fillMaxHeight()) {
                    Log.d(TAG, "onCreate: main")
                    MainScreen(
                            mainViewModel
                    ){
                        handleInteractionEvents(it)
                    }

                    }
                }
            }
        }

    override fun onBackPressed() {
        if (mainViewModel.inSearchMode.value){
            mainViewModel.switchToSearchMode(false)
        }else{
            super.onBackPressed()
        }

    }

    fun handleInteractionEvents(
        interactionEvents:MainScreenInteractionEvents
    ){
        when(interactionEvents){
            is MainScreenInteractionEvents.OpenItemDetails -> {
                Log.d(TAG, "handleInteractionEvents: id: $interactionEvents.item.id")
                startActivity(
                    DetailsActivity.newIntent(
                        context = this,
                        imdbId = interactionEvents.item.id,
                        type = interactionEvents.type.label
                    )
                )
            }
            is MainScreenInteractionEvents.AddToWatchList -> {

            }
            is MainScreenInteractionEvents.RemoveFromWatchList -> {

            }
        }
    }


}

