package com.eco.cleanup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.util.AssetHelper

/**
 * Background gambar dari assets/background/.
 * Gunakan sebagai wrapper layar penuh.
 */
@Composable
fun BackgroundAsset(
    assetPath : String,
    modifier  : Modifier = Modifier,
    content   : @Composable () -> Unit
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(AssetHelper.assetUri(assetPath))
            .build()
    )

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter            = painter,
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )
        content()
    }
}

