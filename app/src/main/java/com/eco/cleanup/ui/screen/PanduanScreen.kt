package com.eco.cleanup.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

// Warna dari palette aplikasi:
// oranye gelap = border panel (#C46A1F), oranye terang = highlight panel (#FFC85A)
private val IsiGelap      = Color(0xFFC46A1F)
private val IsiTerang     = Color(0xFFFFC85A)
private val TeksGelap     = Color.White
private val TeksTerang    = Color(0xFF4A2B0F)   // coklat tua, sama seperti judul MENU di beranda
private val TabPasifBg    = OrangePanel         // #F5A228
private val TabPasifTeks  = Color(0xFF4A2B0F)

@Composable
fun PanduanScreen(onKembali: () -> Unit) {
    var tabTerpilih by remember { mutableIntStateOf(0) } // 0 = Cara Bermain, 1 = Jenis Sampah

    // Cara Bermain -> kotak oranye gelap, Jenis Sampah -> kotak oranye terang
    val isiColor by animateColorAsState(
        targetValue = if (tabTerpilih == 0) IsiGelap else IsiTerang,
        label       = "warna kotak isi"
    )
    val teksUtama by animateColorAsState(
        targetValue = if (tabTerpilih == 0) TeksGelap else TeksTerang,
        label       = "warna teks isi"
    )
    val teksSekunder by animateColorAsState(
        targetValue = (if (tabTerpilih == 0) TeksGelap else TeksTerang).copy(alpha = 0.88f),
        label       = "warna teks sekunder"
    )

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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Strip tab menempel langsung dengan kotak isi (pola codelab)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(isiColor)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            PanduanTab(
                                text         = "Cara Bermain",
                                selected     = tabTerpilih == 0,
                                activeBg     = isiColor,
                                activeText   = teksUtama,
                                onClick      = { tabTerpilih = 0 },
                                modifier     = Modifier.weight(1f)
                            )
                            PanduanTab(
                                text         = "Jenis Sampah",
                                selected     = tabTerpilih == 1,
                                activeBg     = isiColor,
                                activeText   = teksUtama,
                                onClick      = { tabTerpilih = 1 },
                                modifier     = Modifier.weight(1f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 14.dp)
                        ) {
                            Crossfade(targetState = tabTerpilih, label = "isi panduan") { tab ->
                                if (tab == 0) {
                                    IsiCaraBermain(teksUtama)
                                } else {
                                    IsiJenisSampah(teksUtama, teksSekunder)
                                }
                            }
                        }
                    }

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
private fun PanduanTab(
    text       : String,
    selected   : Boolean,
    activeBg   : Color,
    activeText : Color,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    val background by animateColorAsState(
        targetValue = if (selected) activeBg else TabPasifBg,
        label       = "warna latar tab"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) activeText else TabPasifTeks,
        label       = "warna teks tab"
    )

    Box(
        modifier = modifier
            .height(44.dp)
            .background(background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = text,
            fontFamily = FredokaFamily,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            fontSize   = 13.sp,
            color      = textColor
        )
    }
}

@Composable
private fun IsiCaraBermain(teksColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BulletItem("Baca informasi tentang sampah yang muncul.", teksColor)
        BulletItem("Seret sampah ke tong yang sesuai kategorinya.", teksColor)
        BulletItem("Jawaban benar = +10 poin, salah = kurangi 1 nyawa.", teksColor)
        BulletItem("Kumpulkan bintang sebanyak mungkin!", teksColor)
        BulletItem("Selesaikan semua level yang tersedia.", teksColor)
    }
}

@Composable
private fun IsiJenisSampah(teksColor: Color, teksSekunder: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        KategoriItem(
            color     = TongHijau,
            nama      = "Organik",
            desc      = "Sisa makanan, daun, dan bahan alami yang mudah terurai.",
            teksColor = teksColor,
            descColor = teksSekunder
        )
        Spacer(modifier = Modifier.height(8.dp))
        KategoriItem(
            color     = TongBiru,
            nama      = "Anorganik",
            desc      = "Plastik, kaca, logam, dan bahan yang sulit terurai.",
            teksColor = teksColor,
            descColor = teksSekunder
        )
        Spacer(modifier = Modifier.height(8.dp))
        KategoriItem(
            color     = TongMerah,
            nama      = "B3",
            desc      = "Bahan Berbahaya dan Beracun seperti baterai, obat, lampu.",
            teksColor = teksColor,
            descColor = teksSekunder
        )
    }
}

@Composable
private fun BulletItem(text: String, teksColor: Color) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text       = "• ",
            fontFamily = PlusJakartaSansFamily,
            fontSize   = 13.sp,
            color      = teksColor
        )
        Text(
            text       = text,
            fontFamily = PlusJakartaSansFamily,
            fontSize   = 13.sp,
            color      = teksColor
        )
    }
}

@Composable
private fun KategoriItem(
    color     : Color,
    nama      : String,
    desc      : String,
    teksColor : Color,
    descColor : Color
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.Top,
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
                color      = teksColor
            )
            Text(
                text       = desc,
                fontFamily = PlusJakartaSansFamily,
                fontSize   = 12.sp,
                color      = descColor
            )
        }
    }
}
