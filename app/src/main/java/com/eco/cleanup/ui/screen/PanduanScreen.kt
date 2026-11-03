package com.eco.cleanup.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.PapanPanel
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.OrangePanel
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily
import com.eco.cleanup.ui.theme.TongBiru
import com.eco.cleanup.ui.theme.TongHijau
import com.eco.cleanup.ui.theme.TongMerah

@Composable
fun PanduanScreen(onKembali: () -> Unit) {
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
                        text       = "PANDUAN",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 24.sp,
                        color      = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cara Bermain
                    SectionTitle("Cara Bermain")
                    Spacer(modifier = Modifier.height(6.dp))
                    BulletItem("Baca informasi tentang sampah yang muncul.")
                    BulletItem("Seret sampah ke tong yang sesuai kategorinya.")
                    BulletItem("Jawaban benar = +10 poin, salah = kurangi 1 nyawa.")
                    BulletItem("Kumpulkan bintang sebanyak mungkin!")
                    BulletItem("Selesaikan semua level yang tersedia.")

                    Spacer(modifier = Modifier.height(14.dp))

                    // Jenis Sampah
                    SectionTitle("Jenis Sampah")
                    Spacer(modifier = Modifier.height(8.dp))
                    KategoriItem(
                        color = TongHijau,
                        nama  = "Organik",
                        desc  = "Sisa makanan, daun, dan bahan alami yang mudah terurai."
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    KategoriItem(
                        color = TongBiru,
                        nama  = "Anorganik",
                        desc  = "Plastik, kaca, logam, dan bahan yang sulit terurai."
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    KategoriItem(
                        color = TongMerah,
                        nama  = "B3",
                        desc  = "Bahan Berbahaya dan Beracun seperti baterai, obat, lampu."
                    )

                    Spacer(modifier = Modifier.height(20.dp))

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
private fun SectionTitle(text: String) {
    Text(
        text       = text,
        fontFamily = FredokaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 16.sp,
        color      = Color.White
    )
}

@Composable
private fun BulletItem(text: String) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text       = "• ",
            fontFamily = PlusJakartaSansFamily,
            fontSize   = 13.sp,
            color      = Color.White
        )
        Text(
            text       = text,
            fontFamily = PlusJakartaSansFamily,
            fontSize   = 13.sp,
            color      = Color.White
        )
    }
}

@Composable
private fun KategoriItem(color: Color, nama: String, desc: String) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, CircleShape)
                .padding(top = 2.dp)
        )
        Column {
            Text(
                text       = nama,
                fontFamily = FredokaFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = Color.White
            )
            Text(
                text       = desc,
                fontFamily = PlusJakartaSansFamily,
                fontSize   = 12.sp,
                color      = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

