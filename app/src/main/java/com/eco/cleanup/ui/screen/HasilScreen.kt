package com.eco.cleanup.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.BintangRow
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.theme.BtnHijau
import com.eco.cleanup.ui.theme.BtnMerah
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.OrangePanel
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily
import com.eco.cleanup.ui.theme.SkorKuning
import com.eco.cleanup.ui.viewmodel.HasilViewModel

@Composable
fun HasilScreen(
    viewModel   : HasilViewModel,
    onCobaLagi  : () -> Unit,
    onPetaLevel : () -> Unit,
    onNextLevel : (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    BackgroundAsset(assetPath = "background/bg_permainan.png") {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .background(OrangePanel, RoundedCornerShape(24.dp))
                    .border(4.dp, Color.White, RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // Judul
                    Text(
                        text       = if (state.isWin) "LEVEL SELESAI! 🎉" else "LEVEL GAGAL 😞",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 26.sp,
                        color      = Color.White,
                        textAlign  = TextAlign.Center
                    )

                    // Bintang
                    BintangRow(
                        bintang = state.bintang,
                        size    = 44.dp
                    )

                    // Skor
                    Box(
                        modifier = Modifier
                            .background(SkorKuning, RoundedCornerShape(12.dp))
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text       = "Skor",
                                fontFamily = FredokaFamily,
                                fontSize   = 14.sp,
                                color      = Color.White
                            )
                            Text(
                                text       = "${state.skor}",
                                fontFamily = FredokaFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 32.sp,
                                color      = Color.White
                            )
                        }
                    }

                    // Nomor level
                    Text(
                        text       = "Level ${state.levelId} dari 10",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize   = 13.sp,
                        color      = Color.White.copy(alpha = 0.85f)
                    )

                    // Tips / Tahukah Kamu
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text       = if (state.isWin) "💡 Tahukah kamu?" else "💡 Tips:",
                                fontFamily = FredokaFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 14.sp,
                                color      = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text       = state.tips,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize   = 12.sp,
                                color      = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tombol aksi
                    if (state.isWin) {
                        // Menang
                        state.nextLevelId?.let { nextId ->
                            CleanUpButton(
                                text      = "LEVEL SELANJUTNYA",
                                assetPath = "button/bg_btn_hijau.svg",
                                onClick   = { onNextLevel(nextId) },
                                modifier  = Modifier.fillMaxWidth(),
                                fontSize  = 15
                            )
                        }
                        CleanUpButton(
                            text      = "PETA LEVEL",
                            assetPath = "button/bg_btn_biru.svg",
                            onClick   = onPetaLevel,
                            modifier  = Modifier.fillMaxWidth(),
                            fontSize  = 15
                        )
                    } else {
                        // Kalah
                        CleanUpButton(
                            text      = "COBA LAGI",
                            assetPath = "button/bg_btn_hijau.svg",
                            onClick   = onCobaLagi,
                            modifier  = Modifier.fillMaxWidth()
                        )
                        CleanUpButton(
                            text      = "PETA LEVEL",
                            assetPath = "button/bg_btn_biru.svg",
                            onClick   = onPetaLevel,
                            modifier  = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

