package com.eco.cleanup.ui.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.PapanPanel
import com.eco.cleanup.util.AssetHelper
import com.eco.cleanup.util.AudioManager

@Composable
fun BerandaScreen(
    onPlayClick       : () -> Unit,
    onPanduanClick    : () -> Unit,
    onPengaturanClick : () -> Unit
) {
    val context = LocalContext.current

    // Mulai backsound di Beranda
    DisposableEffect(Unit) {
        AudioManager.startBacksound(context, enabled = true)
        onDispose { /* biarkan terus */ }
    }

    BackgroundAsset(assetPath = "background/bg_beranda.png") {
        Column(
            modifier              = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
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
                modifier           = Modifier.size(160.dp),
                contentScale       = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Panel menu dengan papan
            PapanPanel(
                assetPath = "papan/bg_panel_menu.svg",
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Column(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalAlignment   = Alignment.CenterHorizontally,
                    verticalArrangement   = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    CleanUpButton(
                        text      = "MAIN",
                        assetPath = "button/bg_btn_hijau.svg",
                        onClick   = onPlayClick,
                        modifier  = Modifier.fillMaxWidth(0.85f)
                    )

                    CleanUpButton(
                        text      = "PANDUAN",
                        assetPath = "button/bg_btn_biru.svg",
                        onClick   = onPanduanClick,
                        modifier  = Modifier.fillMaxWidth(0.85f)
                    )

                    CleanUpButton(
                        text      = "PENGATURAN",
                        assetPath = "button/bg_btn_coklat.svg",
                        onClick   = onPengaturanClick,
                        modifier  = Modifier.fillMaxWidth(0.85f)
                    )
                }
            }
        }
    }
}

