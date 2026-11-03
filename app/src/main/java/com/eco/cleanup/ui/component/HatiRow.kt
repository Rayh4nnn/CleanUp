package com.eco.cleanup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.util.AssetHelper
import com.eco.cleanup.util.svgImageLoader

/**
 * Menampilkan 3 ikon hati sesuai sisa nyawa.
 */
@Composable
fun HatiRow(
    nyawa   : Int,
    maxNyawa: Int  = 3,
    size    : Dp   = 28.dp,
    modifier: Modifier = Modifier
) {
    val context     = LocalContext.current
    val imageLoader = svgImageLoader(context)

    Row(
        modifier            = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment   = Alignment.CenterVertically
    ) {
        repeat(maxNyawa) { index ->
            val assetPath = if (index < nyawa) "icon/ic_hati.svg" else "icon/ic_hati_abu.svg"
            val painter   = rememberAsyncImagePainter(
                model       = ImageRequest.Builder(context)
                    .data(AssetHelper.assetUri(assetPath))
                    .build(),
                imageLoader = imageLoader
            )
            Image(
                painter            = painter,
                contentDescription = if (index < nyawa) "Nyawa aktif" else "Nyawa habis",
                modifier           = Modifier.size(size)
            )
        }
    }
}

/**
 * Baris bintang untuk screen Hasil.
 */
@Composable
fun BintangRow(
    bintang   : Int,
    maxBintang: Int  = 3,
    size      : Dp   = 40.dp,
    modifier  : Modifier = Modifier
) {
    val context     = LocalContext.current
    val imageLoader = svgImageLoader(context)

    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(maxBintang) { index ->
            val assetPath = "icon/ic_bintang.svg"
            val painter   = rememberAsyncImagePainter(
                model       = ImageRequest.Builder(context)
                    .data(AssetHelper.assetUri(assetPath))
                    .build(),
                imageLoader = imageLoader
            )
            val alpha = if (index < bintang) 1f else 0.25f
            Image(
                painter            = painter,
                contentDescription = "Bintang ${index + 1}",
                modifier           = Modifier
                    .size(size)
                    .then(Modifier),
                alpha              = alpha
            )
        }
    }
}

