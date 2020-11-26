package com.example.imdbviewer.ui.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.imageResource

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.imdbviewer.R


@Composable
fun ImageFromGlide(
url:String,
modifier: Modifier =Modifier,
){
    val placeholder =  imageResource(id = R.drawable.header)
    val (image, setImage) = remember { mutableStateOf(placeholder) }
    Glide.with(ContextAmbient.current)
        .asBitmap()
        .load(url)
        .into(object :CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                setImage(resource.asImageAsset())
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                setImage(placeholder)
            }
        })

    Image(asset = image,modifier = modifier)
}