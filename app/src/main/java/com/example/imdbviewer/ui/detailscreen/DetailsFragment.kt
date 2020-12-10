package com.example.imdbviewer.ui.detailscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.theme.IMDBViewerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.imdbviewer.R
import com.example.imdbviewer.ui.mainscreen.ScreenNavigationEvents

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val detailViewModel: DetailViewModel by viewModels()

    private val categoryType by lazy {
        when (arguments?.getString("categoryType")) {
            CategoryType.Movies.label -> CategoryType.Movies
            CategoryType.TVs.label -> CategoryType.TVs
            else -> null
        }
    }

    private val tmdbId by lazy {
        arguments?.getInt("tmdbId",0)?:0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            if ( categoryType==null || tmdbId==0 ){
                findNavController().popBackStack()
            }else {
                detailViewModel.prepareDetailScreenViewState(tmdbId = tmdbId, type = categoryType!!)
                setContent {
                    IMDBViewerTheme(darkTheme = true) {
                        Surface(
                            color = MaterialTheme.colors.background,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            DetailsScreen(viewModel = detailViewModel,navController = findNavController())
                        }
                    }
                }
            }
        }
        return view
    }


}