package com.eco.cleanup.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.data.local.entity.SampahEntity
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.HatiRow
import com.eco.cleanup.ui.theme.BtnBiru
import com.eco.cleanup.ui.theme.BtnHijau
import com.eco.cleanup.ui.theme.BtnMerah
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.OrangePanel
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily
import com.eco.cleanup.ui.theme.SkorKuning
import com.eco.cleanup.ui.viewmodel.GameplayViewModel
import com.eco.cleanup.util.AssetHelper
import com.eco.cleanup.util.svgImageLoader
import kotlin.math.roundToInt

@Composable
fun GameplayScreen(
    viewModel   : GameplayViewModel,
    onHasil     : (Boolean, Int, Int) -> Unit,
    onBackToPeta: () -> Unit
) {
    val state   by viewModel.state.collectAsState()
    val context = LocalContext.current
    val svgLoader = remember { svgImageLoader(context) }

    // Navigasi ke Hasil saat game selesai (menang atau kalah)
    LaunchedEffect(state.isDone, state.showSalahModal, state.showSuccessModal) {
        if (state.isDone && !state.showSalahModal && !state.showSuccessModal) {
            onHasil(state.isWin, state.skor, viewModel.getBintang())
        }
    }

    // Koordinat bounding box untuk masing-masing 3 tong sampah
    val tongBounds = remember { mutableStateMapOf<Int, Rect>() }

    // Posisi drag item sampah
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var itemRestCenter by remember { mutableStateOf(Offset.Zero) }

    BackgroundAsset(assetPath = "background/bg_permainan.png") {
        Box(modifier = Modifier.fillMaxSize()) {

            // ── 1. HUD Atas (Nyawa & Tombol Menu) ──────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Ikon Nyawa (Hati)
                HatiRow(nyawa = state.nyawa, size = 32.dp)

                // Tombol Menu (Berfungsi membuka menu pause)
                val menuPainter = rememberAsyncImagePainter(
                    model       = ImageRequest.Builder(context)
                        .data(AssetHelper.assetUri("icon/ic_menu.svg"))
                        .build(),
                    imageLoader = svgLoader
                )
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.onPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter            = menuPainter,
                        contentDescription = "Menu Pause",
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Fit
                    )
                }
            }

            // ── 2. Card Edukasi Teks (Di atas trotoar) ─────────────────
            state.currentSampah?.let { sampah ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp, start = 20.dp, end = 20.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = sampah.teksEdukasi,
                            style      = TextStyle(
                                fontFamily    = PlusJakartaSansFamily,
                                fontWeight    = FontWeight.Bold,
                                fontSize      = 13.sp,
                                color         = Color(0xFF1E293B),
                                textAlign     = TextAlign.Center,
                                lineHeight    = 18.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                }
            }

            // ── 3. Tiga Tong Sampah Terpisah di Trotoar ───────────────
            // Organik (Hijau: 1), Anorganik (Biru: 2), B3 (Merah: 3)
            val tongList = remember {
                listOf(
                    Triple(1, "item/tempat_sampah_hijau.svg", "Organik"),
                    Triple(2, "item/tempat_sampah_biru.svg",  "Anorganik"),
                    Triple(3, "item/tempat_sampah_merah.svg", "B3")
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .offset(y = (-10).dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Bottom
            ) {
                tongList.forEach { (id, assetPath, label) ->
                    val tongPainter = rememberAsyncImagePainter(
                        model       = ImageRequest.Builder(context)
                            .data(AssetHelper.assetUri(assetPath))
                            .build(),
                        imageLoader = svgLoader
                    )

                    Box(
                        modifier = Modifier
                            .width(105.dp)
                            .height(155.dp)
                            .onGloballyPositioned { coords ->
                                tongBounds[id] = coords.boundsInRoot()
                            },
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter            = tongPainter,
                            contentDescription = "Tempat Sampah $label",
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Fit
                        )
                    }
                }
            }

            // ── 4. Item Sampah (Di Bawah pada Aspal Jalan) ─────────────
            state.currentSampah?.let { sampah ->
                val itemPainter = rememberAsyncImagePainter(
                    model       = ImageRequest.Builder(context)
                        .data(AssetHelper.assetUri(sampah.asetGambar))
                        .build(),
                    imageLoader = svgLoader
                )

                // Hanya tampilkan jika tidak sedang tertutup modal sukses
                if (!state.showSuccessModal) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 85.dp)
                            .offset {
                                IntOffset(
                                    dragOffsetX.roundToInt(),
                                    dragOffsetY.roundToInt()
                                )
                            }
                            .size(110.dp)
                            .onGloballyPositioned { coords ->
                                if (dragOffsetX == 0f && dragOffsetY == 0f) {
                                    itemRestCenter = coords.boundsInRoot().center
                                }
                            }
                            .pointerInput(sampah.idSampah) {
                                detectDragGestures(
                                    onDragStart = { /* mulai drag */ },
                                    onDragEnd   = {
                                        val currentCenter = itemRestCenter + Offset(dragOffsetX, dragOffsetY)

                                        if (tongBounds.size >= 3) {
                                            val rect1 = tongBounds[1]
                                            val rect2 = tongBounds[2]
                                            val rect3 = tongBounds[3]

                                            if (rect1 != null && rect2 != null && rect3 != null) {
                                                // Area vertikal tong sampah (dari tutup/lid atas sampai dasar roda)
                                                val topLimit = minOf(rect1.top, rect2.top, rect3.top) - 100f
                                                val bottomLimit = maxOf(rect1.bottom, rect2.bottom, rect3.bottom) + 80f

                                                // Cek apakah sampah di-drag sampai ke ketinggian trotoar / tong
                                                if (currentCenter.y in topLimit..bottomLimit) {
                                                    // Deteksi secara presisi berdasarkan posisi horizontal tong terdekat
                                                    val dist1 = kotlin.math.abs(currentCenter.x - rect1.center.x)
                                                    val dist2 = kotlin.math.abs(currentCenter.x - rect2.center.x)
                                                    val dist3 = kotlin.math.abs(currentCenter.x - rect3.center.x)

                                                    val chosenTongId = when {
                                                        dist1 <= dist2 && dist1 <= dist3 -> 1
                                                        dist3 <= dist1 && dist3 <= dist2 -> 3
                                                        else -> 2
                                                    }
                                                    viewModel.onItemDropped(chosenTongId)
                                                }
                                            }
                                        }

                                        // Reset posisi drag kembali ke aspal jalan
                                        dragOffsetX = 0f
                                        dragOffsetY = 0f
                                    },
                                    onDragCancel = {
                                        dragOffsetX = 0f
                                        dragOffsetY = 0f
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        dragOffsetX += dragAmount.x
                                        dragOffsetY += dragAmount.y
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter            = itemPainter,
                            contentDescription = sampah.namaSampah,
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Fit
                        )
                    }
                }
            }

            // ── 5. Modal Popup Sukses +10 (Panel 3 di Figma) ───────────
            AnimatedVisibility(
                visible  = state.showSuccessModal && state.currentSampah != null,
                enter    = fadeIn() + scaleIn(),
                exit     = fadeOut() + scaleOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                state.currentSampah?.let { sampah ->
                    SuccessModal(
                        sampah      = sampah,
                        onNextClick = {
                            viewModel.onNextClicked()
                        }
                    )
                }
            }

            // ── 6. Modal Popup Salah ──────────────────────────────────
            AnimatedVisibility(
                visible  = state.showSalahModal && state.currentSampah != null,
                enter    = fadeIn() + scaleIn(),
                exit     = fadeOut() + scaleOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                state.currentSampah?.let { sampah ->
                    SalahModal(
                        sampah      = sampah,
                        isGameOver  = state.nyawa <= 0,
                        onNextClick = {
                            if (state.nyawa <= 0) {
                                onHasil(false, state.skor, 0)
                            } else {
                                viewModel.onNextClicked()
                            }
                        }
                    )
                }
            }

            // ── 7. Pause Dialog ───────────────────────────────────────
            if (state.isPaused) {
                PauseDialog(
                    onLanjutkan = viewModel::onResume,
                    onUlang     = {
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                        viewModel.onRestart()
                    },
                    onKeluar    = onBackToPeta
                )
            }
        }
    }
}

// ── Modal Sukses +10 (Sesuai Panel 3 Figma) ───────────────────────────────────

@Composable
private fun SuccessModal(
    sampah      : SampahEntity,
    onNextClick : () -> Unit
) {
    val context   = LocalContext.current
    val svgLoader = remember { svgImageLoader(context) }

    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth(0.86f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Container Pop-up dengan Ikon Bintang +10 di bagian atas yang menimpa tepi
            Box(
                modifier         = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                // ── Kotak Biru Muda (Single Container Sesuai Figma) ──
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    shape           = RoundedCornerShape(22.dp),
                    color           = Color(0xFFC8F0FF),
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 38.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Teks Edukasi (Bold, Center)
                        Text(
                            text       = sampah.teksEdukasi,
                            style      = TextStyle(
                                fontFamily    = PlusJakartaSansFamily,
                                fontWeight    = FontWeight.Bold,
                                fontSize      = 13.sp,
                                color         = Color(0xFF1E293B),
                                textAlign     = TextAlign.Center,
                                lineHeight    = 18.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )

                        // Ilustrasi Item Sampah
                        val itemPainter = rememberAsyncImagePainter(
                            model       = ImageRequest.Builder(context)
                                .data(AssetHelper.assetUri(sampah.asetGambar))
                                .build(),
                            imageLoader = svgLoader
                        )
                        Image(
                            painter            = itemPainter,
                            contentDescription = sampah.namaSampah,
                            modifier           = Modifier.size(110.dp),
                            contentScale       = ContentScale.Fit
                        )
                    }
                }

                // ── Ikon Bintang +10 (ic_bintang.svg) Menimpa Tepi Atas ──
                val starPainter = rememberAsyncImagePainter(
                    model       = ImageRequest.Builder(context)
                        .data(AssetHelper.assetUri("icon/ic_bintang.svg"))
                        .build(),
                    imageLoader = svgLoader
                )
                Image(
                    painter            = starPainter,
                    contentDescription = "+10",
                    modifier           = Modifier
                        .width(135.dp)
                        .height(74.dp),
                    contentScale       = ContentScale.Fit
                )
            }

            // ── Tombol Biru SELANJUTNYA Di Luar & Di Bawah Kotak ──
            CleanUpButton(
                text      = "SELANJUTNYA",
                assetPath = "button/bg_btn_biru.svg",
                onClick   = onNextClick,
                modifier  = Modifier.fillMaxWidth(0.92f),
                height    = 50.dp,
                fontSize  = 16
            )
        }
    }
}

// ── Modal Feedback Salah ──────────────────────────────────────────────────────

@Composable
private fun SalahModal(
    sampah      : SampahEntity,
    isGameOver  : Boolean,
    onNextClick : () -> Unit
) {
    val context   = LocalContext.current
    val svgLoader = remember { svgImageLoader(context) }

    val kategoriNama = when (sampah.idKategori) {
        1 -> "Organik (Hijau)"
        2 -> "Anorganik (Biru)"
        else -> "B3 (Merah)"
    }

    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth(0.86f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Surface(
                modifier        = Modifier.fillMaxWidth(),
                shape           = RoundedCornerShape(22.dp),
                color           = Color(0xFFFFEEEE),
                border          = BorderStroke(2.dp, BtnMerah),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text       = if (isGameOver) "💔 NYAWA HABIS!" else "❌ UPS, SALAH TONG!",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        color      = BtnMerah
                    )

                    Text(
                        text       = "${sampah.namaSampah} seharusnya dimasukkan ke tong $kategoriNama.",
                        style      = TextStyle(
                            fontFamily    = PlusJakartaSansFamily,
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 13.sp,
                            color         = Color(0xFF333333),
                            textAlign     = TextAlign.Center,
                            lineHeight    = 18.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )

                    // Ilustrasi Item Sampah
                    val painter = rememberAsyncImagePainter(
                        model       = ImageRequest.Builder(context)
                            .data(AssetHelper.assetUri(sampah.asetGambar))
                            .build(),
                        imageLoader = svgLoader
                    )
                    Image(
                        painter            = painter,
                        contentDescription = sampah.namaSampah,
                        modifier           = Modifier.size(90.dp),
                        contentScale       = ContentScale.Fit
                    )
                }
            }

            CleanUpButton(
                text      = if (isGameOver) "LIHAT HASIL" else "SELANJUTNYA",
                assetPath = if (isGameOver) "button/bg_btn_merah.svg" else "button/bg_btn_hijau.svg",
                onClick   = onNextClick,
                modifier  = Modifier.fillMaxWidth(0.92f),
                height    = 50.dp,
                fontSize  = 16
            )
        }
    }
}

// ── Dialog Pause ──────────────────────────────────────────────────────────────

@Composable
private fun PauseDialog(
    onLanjutkan : () -> Unit,
    onUlang     : () -> Unit,
    onKeluar    : () -> Unit
) {
    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .background(OrangePanel, RoundedCornerShape(20.dp))
                .border(3.dp, Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text       = "PAUSE",
                    fontFamily = FredokaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 28.sp,
                    color      = Color.White
                )

                CleanUpButton(
                    text      = "LANJUTKAN",
                    assetPath = "button/bg_btn_hijau.svg",
                    onClick   = onLanjutkan,
                    modifier  = Modifier.fillMaxWidth()
                )

                CleanUpButton(
                    text      = "ULANG",
                    assetPath = "button/bg_btn_biru.svg",
                    onClick   = onUlang,
                    modifier  = Modifier.fillMaxWidth()
                )

                CleanUpButton(
                    text      = "KELUAR",
                    assetPath = "button/bg_btn_merah.svg",
                    onClick   = onKeluar,
                    modifier  = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
