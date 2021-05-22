package com.example.neugelb.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.request.CachePolicy
import com.example.neugelb.compose.component.text.BodyText
import com.example.neugelb.apis.POSTER_PATH_URL_API
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.SixteenDp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun MovieBackgroundImage(
    modifier: Modifier = Modifier,
    title: String,
    url: String?,
    backgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Box(modifier = modifier.background(color = backgroundColor)) {
        val painter =
            rememberCoilPainter(
                request = POSTER_PATH_URL_API + url,
                fadeIn = true,
                requestBuilder = {
                    val cachePolicy = CachePolicy.ENABLED
                    diskCachePolicy(cachePolicy)
                    memoryCachePolicy(cachePolicy)
                    networkCachePolicy(CachePolicy.ENABLED)
                })
        Image(
            painter = painter,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f),
            contentDescription = "Movie image",
            contentScale = ContentScale.Fit
        )
        when (painter.loadState) {
            ImageLoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(SixteenDp)
                        .align(Alignment.Center), color = NeugelbTheme.colors.mainColor
                )
            }
            is ImageLoadState.Error -> {
                val text = title
                val textModifier = Modifier.align(Alignment.Center)
                BodyText(modifier = textModifier, text = text, color = textColor)
            }
        }
    }
}
