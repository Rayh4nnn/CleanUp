package com.eco.cleanup.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.PapanPanel
import com.eco.cleanup.ui.theme.FredokaFamily
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
        AudioManager.startBacksound(context)
        onDispose { /* biarkan terus */ }
    }

    // Menggunakan asset background beranda baru (logo sudah ada di dalam gambar)
    BackgroundAsset(assetPath = "background/bg_beranda.png") {
        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Panel menu papan kayu oranye (posisi pas di bawah logo dan di atas tong sampah)
            PapanPanel(
                assetPath = "papan/bg_panel_menu.svg",
                modifier  = Modifier
                    .fillMaxWidth(0.84f)
                    .height(320.dp)
                    .offset(y = (-20).dp)
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text       = "MENU",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 24.sp,
                        color      = Color(0xFF4A2B0F)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    CleanUpButton(
                        text      = "MAIN",
                        assetPath = "button/bg_btn_hijau.svg",
                        onClick   = onPlayClick,
                        modifier  = Modifier.fillMaxWidth(0.92f),
                        height    = 52.dp
                    )

                    CleanUpButton(
                        text      = "PANDUAN",
                        assetPath = "button/bg_btn_hijau.svg",
                        onClick   = onPanduanClick,
                        modifier  = Modifier.fillMaxWidth(0.92f),
                        height    = 52.dp
                    )

                    CleanUpButton(
                        text      = "PENGATURAN",
                        assetPath = "button/bg_btn_hijau.svg",
                        onClick   = onPengaturanClick,
                        modifier  = Modifier.fillMaxWidth(0.92f),
                        height    = 52.dp
                    )
                }
            }
        }
    }
}
