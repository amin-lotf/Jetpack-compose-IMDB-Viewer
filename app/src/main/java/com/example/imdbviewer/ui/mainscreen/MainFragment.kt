package com.example.imdbviewer.ui.mainscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.imdbviewer.R
import com.example.imdbviewer.theme.IMDBViewerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFocus
@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MainFragment:Fragment() {
    private val TAG = "aminjoon"

    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view= ComposeView(requireContext())
        view.apply {
                    setContent {
            IMDBViewerTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme

                Surface(color = MaterialTheme.colors.background,modifier = Modifier.fillMaxHeight()) {
                    Log.d(TAG, "onCreate: main")
                    MainScreen(
                            mainViewModel
                    ){
                        handleNavigationEvents(it)
                    }

                    }
                }
            }

        }
        return view
    }


    fun handleNavigationEvents(
        navigationEvents:ScreenNavigationEvents
    ){
        when(navigationEvents){
            is ScreenNavigationEvents.NavigateToItemDetails -> {
                val bundle= bundleOf("tmdbId" to navigationEvents.item.id, "categoryType" to navigationEvents.item.category)
                findNavController().navigate(R.id.action_mainFragment_to_detailsFragment,bundle)

            }
           ScreenNavigationEvents.NavigateToFavorites->{
               findNavController().navigate(R.id.action_mainFragment_to_nav_favorites)
           }
        }
    }
}