package com.eco.cleanup.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    onPengaturanClick : () -> Unit,
    onAboutClick      : () -> Unit
) {
    val context = LocalContext.current

    // Mulai backsound di Beranda
    DisposableEffect(Unit) {
        AudioManager.startBacksound(context)
        onDispose { /* biarkan terus */ }
    }

    // Menggunakan asset background beranda baru (logo sudah ada di dalam gambar)
    BackgroundAsset(assetPath = "background/bg_beranda.png") {
        BoxWithConstraints(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Panel menyesuaikan tinggi layar agar tidak terpotong di layar kecil / landscape
            val compact         = maxHeight < 600.dp
            val panelHeight     = (maxHeight * 0.85f).coerceIn(200.dp, 400.dp)
            val itemSpacing     = if (compact) 10.dp else 14.dp
            val titleFontSize   = if (compact) 20 else 24

            // 40dp = padding vertikal isi panel, sisanya judul + spacer + jarak antar item
            val panelPadding    = 40.dp
            val titleBlock      = (if (compact) 28.dp else 34.dp) + 2.dp
            val rawButtonHeight = (panelHeight - panelPadding - titleBlock - itemSpacing * 4) / 4
            val buttonHeight    = rawButtonHeight.coerceIn(38.dp, 52.dp)
            val needScroll      = rawButtonHeight < 38.dp

            // Panel menu papan kayu oranye (posisi pas di bawah logo dan di atas tong sampah)
            PapanPanel(
                assetPath       = "papan/bg_panel_menu.svg",
                modifier        = Modifier
                    .fillMaxWidth(0.84f)
                    .height(panelHeight)
                    .offset(y = if (compact) 0.dp else (-20).dp),
                contentModifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxSize()
                        .then(
                            if (needScroll) Modifier.verticalScroll(rememberScrollState())
                            else Modifier
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (needScroll) Arrangement.Top else Arrangement.Center
                ) {
                    Text(
                        text       = "MENU",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = titleFontSize.sp,
                        color      = Color(0xFF4A2B0F)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Column(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(itemSpacing)
                    ) {
                        CleanUpButton(
                            text      = "MAIN",
                            assetPath = "button/bg_btn_hijau.svg",
                            onClick   = onPlayClick,
                            modifier  = Modifier.fillMaxWidth(0.92f),
                            height    = buttonHeight
                        )

                        CleanUpButton(
                            text      = "PANDUAN",
                            assetPath = "button/bg_btn_hijau.svg",
                            onClick   = onPanduanClick,
                            modifier  = Modifier.fillMaxWidth(0.92f),
                            height    = buttonHeight
                        )

                        CleanUpButton(
                            text      = "PENGATURAN",
                            assetPath = "button/bg_btn_hijau.svg",
                            onClick   = onPengaturanClick,
                            modifier  = Modifier.fillMaxWidth(0.92f),
                            height    = buttonHeight
                        )

                        CleanUpButton(
                            text      = "TENTANG KAMI",
                            assetPath = "button/bg_btn_biru.svg",
                            onClick   = onAboutClick,
                            modifier  = Modifier.fillMaxWidth(0.92f),
                            height    = buttonHeight
                        )
                    }
                }
            }
        }
    }
}
