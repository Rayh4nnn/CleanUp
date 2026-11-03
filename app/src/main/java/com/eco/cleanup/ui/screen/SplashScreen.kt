package com.eco.cleanup.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.theme.BtnHijau
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.util.AssetHelper
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current

    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue   = progress,
        animationSpec = tween(durationMillis = 2000),
        label         = "splash_progress"
    )

    LaunchedEffect(Unit) {
        progress = 1f
        delay(2500)
        onSplashFinished()
    }

    BackgroundAsset(assetPath = "background/bg_beranda.png") {
        Column(
            modifier              = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.Center
        ) {
            // Logo
            val logoPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(AssetHelper.assetUri("logo/logo.png"))
                    .build()
            )
            Image(
                painter            = logoPainter,
                contentDescription = "Logo CleanUp",
                modifier           = Modifier.size(180.dp),
                contentScale       = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text       = "Pilah sampah, selamatkan bumi!",
                fontFamily = FredokaFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 16.sp,
                color      = Color.White
            )

            Spacer(modifier = Modifier.height(48.dp))

            LinearProgressIndicator(
                progress      = { animatedProgress },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color         = BtnHijau,
                trackColor    = Color.White.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text       = "Loading...",
                fontFamily = FredokaFamily,
                fontSize   = 14.sp,
                color      = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

