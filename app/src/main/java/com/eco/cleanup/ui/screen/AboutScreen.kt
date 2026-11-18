package com.eco.cleanup.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.PapanPanel
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily

private data class AnggotaKelompok(
    val nama : String,
    val nim  : String
)

private val anggotaKelompok2 = listOf(
    AnggotaKelompok("Habibi Kutai Kartanegara", "246651032"),
    AnggotaKelompok("Muhammad Rizqon Pratama", "246651035"),
    AnggotaKelompok("Muhammad Rayhan Firdaus", "246651036"),
    AnggotaKelompok("Dicko Afrillian Hernando", "246651054"),
    AnggotaKelompok("Muhammad Fadil Agazi", "246652004")
)

@Composable
fun AboutScreen(onKembali: () -> Unit) {
    // -1 = semua item tertutup; selain itu = index nama yang sedang terbuka
    var expandedIndex by remember { mutableIntStateOf(-1) }

    BackgroundAsset(assetPath = "background/bg_beranda.png") {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            PapanPanel(
                assetPath = "papan/bg_panel_panduan.svg",
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text       = "TENTANG KAMI",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 24.sp,
                        color      = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text       = "Permainan edukasi memilah sampah",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize   = 12.sp,
                        color      = Color.White.copy(alpha = 0.9f),
                        textAlign  = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text       = "KELOMPOK 2",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = Color.White
                    )

                    Text(
                        text       = "Prodi Teknik Informatika Multimedia - POLNES",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize   = 12.sp,
                        color      = Color.White.copy(alpha = 0.9f),
                        textAlign  = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    anggotaKelompok2.forEachIndexed { index, anggota ->
                        AnggotaItem(
                            nomor    = index + 1,
                            anggota  = anggota,
                            expanded = expandedIndex == index,
                            onClick  = {
                                expandedIndex = if (expandedIndex == index) -1 else index
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CleanUpButton(
                        text      = "KEMBALI",
                        assetPath = "button/bg_btn_hijau.svg",
                        onClick   = onKembali,
                        modifier  = Modifier.fillMaxWidth(0.7f),
                        height    = 46.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun AnggotaItem(
    nomor    : Int,
    anggota  : AnggotaKelompok,
    expanded : Boolean,
    onClick  : () -> Unit
) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 2.dp, horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            verticalAlignment     = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text       = "$nomor.",
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = Color.White
            )
            Text(
                text       = anggota.nama,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = Color.White
            )
        }

        if (expanded) {
            Text(
                text       = "NIM ${anggota.nim}",
                fontFamily = PlusJakartaSansFamily,
                fontSize   = 12.sp,
                color      = Color.White.copy(alpha = 0.9f),
                modifier   = Modifier.padding(start = 20.dp)
            )
        }
    }
}
