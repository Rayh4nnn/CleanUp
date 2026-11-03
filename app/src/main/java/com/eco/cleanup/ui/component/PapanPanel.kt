package com.eco.cleanup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.util.AssetHelper
import com.eco.cleanup.util.svgImageLoader

/**
 * Panel / papan kayu oranye sebagai container dialog/popup.
 * Menggunakan asset SVG dari papan/.
 */
@Composable
fun PapanPanel(
    assetPath : String = "papan/bg_panel_menu.svg",
    modifier  : Modifier = Modifier,
    content   : @Composable () -> Unit
) {
    val context     = LocalContext.current
    val imageLoader = svgImageLoader(context)
    val painter     = rememberAsyncImagePainter(
        model       = ImageRequest.Builder(context)
            .data(AssetHelper.assetUri(assetPath))
            .build(),
        imageLoader = imageLoader
    )

    Box(
        modifier         = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter            = painter,
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.FillBounds
        )
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
            content()
        }
    }
}

