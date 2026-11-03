package com.eco.cleanup.util

import android.content.Context
import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult

/**
 * Helper untuk load asset dari folder assets/ menggunakan Coil.
 * Path: "item/apel.svg" → "file:///android_asset/item/apel.svg"
 */
object AssetHelper {
    fun assetUri(path: String): String = "file:///android_asset/$path"
}

/**
 * Buat Coil ImageLoader dengan dukungan SVG.
 */
fun svgImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
}

