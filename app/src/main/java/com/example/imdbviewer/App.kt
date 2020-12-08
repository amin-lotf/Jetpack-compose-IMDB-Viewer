package com.example.imdbviewer

import android.app.Application
import com.example.imdbviewer.data.Repository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@HiltAndroidApp
class App:Application()