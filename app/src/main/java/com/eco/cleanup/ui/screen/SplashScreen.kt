package com.eco.cleanup.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.theme.FredokaFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
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

    // Menggunakan asset background splash screen baru (logo sudah ada di dalam gambar)
    BackgroundAsset(assetPath = "background/bg_splashscreen.png") {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Indikator loading bar di bagian bawah (area jalan)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress   = { animatedProgress },
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(7.dp)),
                    color      = Color(0xFFFFD13B),
                    trackColor = Color(0xFF263238).copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text       = "Loading...",
                    fontFamily = FredokaFamily,
                    fontSize   = 14.sp,
                    color      = Color.White
                )
            }
        }
    }
}
