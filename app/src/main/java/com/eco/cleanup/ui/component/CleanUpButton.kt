package com.eco.cleanup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.util.AssetHelper
import com.eco.cleanup.util.svgImageLoader

/**
 * Tombol utama CleanUp dengan background SVG dari assets/button/.
 * Contoh: CleanUpButton("MAIN", "button/bg_btn_hijau.svg", onClick = {})
 */
@Composable
fun CleanUpButton(
    text        : String,
    assetPath   : String,           // mis. "button/bg_btn_hijau.svg"
    onClick     : () -> Unit,
    modifier    : Modifier  = Modifier,
    height      : Dp        = 52.dp,
    textColor   : Color     = Color.White,
    fontSize    : Int       = 18,
    enabled     : Boolean   = true
) {
    val context     = LocalContext.current
    val imageLoader = svgImageLoader(context)
    val painter     = rememberAsyncImagePainter(
        model         = ImageRequest.Builder(context)
            .data(AssetHelper.assetUri(assetPath))
            .build(),
        imageLoader   = imageLoader
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                enabled              = enabled,
                interactionSource    = remember { MutableInteractionSource() },
                indication           = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter             = painter,
            contentDescription  = null,
            modifier            = Modifier.fillMaxSize(),
            contentScale        = ContentScale.FillBounds
        )
        Text(
            text       = text,
            fontFamily = FredokaFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = fontSize.sp,
            color      = textColor,
            textAlign  = TextAlign.Center
        )
    }
}

