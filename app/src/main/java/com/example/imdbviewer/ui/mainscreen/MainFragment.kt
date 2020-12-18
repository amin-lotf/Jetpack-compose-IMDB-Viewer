package com.example.imdbviewer.ui.mainscreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.imdbviewer.R

import com.example.imdbviewer.data.network.firebase.FirebaseAuthUtil
import com.example.imdbviewer.theme.IMDBViewerTheme
import com.example.imdbviewer.util.ScreenNavigationEvents
import com.firebase.ui.auth.IdpResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import androidx.compose.runtime.getValue



@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MainFragment:Fragment() {
    private val TAG = "aminjoon"
    
    private val activityForResult=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        val response=IdpResponse.fromResultIntent(result.data)
        if (result.resultCode==Activity.RESULT_OK){
           mainViewModel.handleUserLogin()
        }else if (result.resultCode==Activity.RESULT_CANCELED){
           response?.let { authResponse->
                   mainViewModel.handleFirebaseError(authResponse.error)
           }
        }
    }

    private val startActivityForPhoto=registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        uri?.let {
            mainViewModel.bufferPhoto(it)
        }

    }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view= ComposeView(requireContext())
        view.apply {
                    setContent {
                        val userPreferences by mainViewModel.userPreferences.collectAsState()
            IMDBViewerTheme(darkTheme = userPreferences.inDarkMode) {
                // A surface container using the 'background' color from the theme


                    Log.d(TAG, "onCreate: main")
                    MainScreen(
                        mainViewModel,
                        inDarkMode = userPreferences.inDarkMode
                    ){
                        handleNavigationEvents(it)
                    }

                    }

            }

        }
        return view
    }


    fun handleNavigationEvents(
        navigationEvents: ScreenNavigationEvents
    ){
        when(navigationEvents){
            is ScreenNavigationEvents.NavigateToItemDetails -> {
                val bundle = bundleOf(
                    "tmdbId" to navigationEvents.item.id,
                    "categoryType" to navigationEvents.item.category
                )
                findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)

            }
            ScreenNavigationEvents.NavigateToFavorites -> {
                findNavController().navigate(R.id.action_mainFragment_to_nav_favorites)
            }
            ScreenNavigationEvents.NavigateToSignInActivity->{
                activityForResult.launch(FirebaseAuthUtil.authIntent)
            }
            ScreenNavigationEvents.NavigateToChoosePhotoActivity->{
                startActivityForPhoto.launch("image/*")
            }
        }
    }
}