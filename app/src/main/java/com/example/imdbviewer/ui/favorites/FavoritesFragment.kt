package com.example.imdbviewer.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.imdbviewer.theme.IMDBViewerTheme
import com.example.imdbviewer.util.ScreenNavigationEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.compose.runtime.getValue

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val favoriteViewModel by viewModels<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                val userPreferences by favoriteViewModel.userPreferences.collectAsState()
                IMDBViewerTheme(darkTheme = userPreferences.inDarkMode) {
                    FavoriteScreen(viewModel = favoriteViewModel) {
                        handleNavigationEvents(it)
                    }
                }
            }
        }
        return view
    }

    fun handleNavigationEvents(
        navigationEvents: ScreenNavigationEvents
    ) {
        when (navigationEvents) {
            is ScreenNavigationEvents.NavigateToItemDetails -> {
                val bundle = bundleOf(
                    "tmdbId" to navigationEvents.item.id,
                    "categoryType" to navigationEvents.item.category
                )
                findNavController().navigate(R.id.action_nav_favorites_to_detailsFragment, bundle)
            }
            ScreenNavigationEvents.NavigateBack -> findNavController().popBackStack()
            else -> {
            }
        }
    }
}