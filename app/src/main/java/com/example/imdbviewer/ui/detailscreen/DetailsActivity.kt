package com.example.imdbviewer.ui.detailscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.example.imdbviewer.data.cache.CategoryType
import com.example.imdbviewer.theme.IMDBViewerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    val detailViewModel:DetailViewModel by viewModels()

    private val tmdbId by lazy {
        intent.getIntExtra(IMDB_ID,0)
    }

    private val categoryType by lazy {
        when(intent.getStringExtra(ITEM_TYPE)){
            CategoryType.Movies.label-> CategoryType.Movies
            CategoryType.TVs.label->CategoryType.TVs
            else -> null
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if ( categoryType==null || tmdbId==0 ){
            finish()
        }else{
            detailViewModel.prepareDetailScreenViewState(tmdbId = tmdbId,type = categoryType!!)
            setContent {
                IMDBViewerTheme(darkTheme = true) {
                    Surface(color = MaterialTheme.colors.background,modifier = Modifier.fillMaxSize()) {
                        DetailScreen(viewModel =detailViewModel)
                    }
                }
            }

        }

    }

    companion object{
        private const val IMDB_ID="imdb id"
        private const val ITEM_TYPE=" item type"
        fun newIntent(context: Context,imdbId:Int,type:String)=
            Intent(context,DetailsActivity::class.java).apply {
                putExtra(IMDB_ID,imdbId)
                putExtra(ITEM_TYPE,type)
            }
    }
}