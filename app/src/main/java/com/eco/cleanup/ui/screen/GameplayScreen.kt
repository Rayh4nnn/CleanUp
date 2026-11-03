package com.eco.cleanup.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

    // Koordinat bounding box untuk ketiga tong sampah (tempat_sampah.svg)
    var tongBounds by remember { mutableStateOf<Rect?>(null) }

    // Posisi drag item sampah
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var itemCenterInRoot by remember { mutableStateOf(Offset.Zero) }

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFFE8E8E8), RoundedCornerShape(16.dp))
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = sampah.teksEdukasi,
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 13.sp,
                        color      = Color(0xFF333333),
                        textAlign  = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            // ── 3. Tiga Tong Sampah Besar (Di Trotoar) ─────────────────
            // Berada di tengah (trotoar), menggantikan 3 kotak kecil sebelumnya
            val tongPainter = rememberAsyncImagePainter(
                model       = ImageRequest.Builder(context)
                    .data(AssetHelper.assetUri("item/tempat_sampah.svg"))
                    .build(),
                imageLoader = svgLoader
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(175.dp)
                    .align(Alignment.Center)
                    .offset(y = (-20).dp)
                    .onGloballyPositioned { coords ->
                        tongBounds = coords.boundsInRoot()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter            = tongPainter,
                    contentDescription = "Tempat Sampah (Organik, Anorganik, B3)",
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Fit
                )
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
                            .padding(bottom = 90.dp)
                            .offset {
                                IntOffset(
                                    dragOffsetX.roundToInt(),
                                    dragOffsetY.roundToInt()
                                )
                            }
                            .size(110.dp)
                            .onGloballyPositioned { coords ->
                                val bounds = coords.boundsInRoot()
                                itemCenterInRoot = bounds.center
                            }
                            .pointerInput(sampah.idSampah) {
                                detectDragGestures(
                                    onDragStart = { /* mulai drag */ },
                                    onDragEnd   = {
                                        val bounds = tongBounds
                                        if (bounds != null) {
                                            val currentCenter = itemCenterInRoot + Offset(dragOffsetX, dragOffsetY)

                                            // Cek apakah item di-drag sampai ke area tong sampah
                                            val tolerance = 80f
                                            val reachedY = currentCenter.y <= (bounds.bottom + tolerance) &&
                                                    currentCenter.y >= (bounds.top - tolerance)

                                            val reachedX = currentCenter.x >= (bounds.left - tolerance) &&
                                                    currentCenter.x <= (bounds.right + tolerance)

                                            if (reachedY && reachedX) {
                                                // Bagi 3 tong secara horizontal:
                                                // Kiri  = Organik (1)
                                                // Tengah = Anorganik (2)
                                                // Kanan = B3 (3)
                                                val third = bounds.width / 3f
                                                val droppedTongId = when {
                                                    currentCenter.x < bounds.left + third -> 1
                                                    currentCenter.x > bounds.left + 2f * third -> 3
                                                    else -> 2
                                                }
                                                viewModel.onItemDropped(droppedTongId)
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
        // Kartu biru muda #C8F0FF persis seperti di Figma
        Box(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .background(Color(0xFFC8F0FF), RoundedCornerShape(22.dp))
                .border(3.dp, Color.White, RoundedCornerShape(22.dp))
                .shadow(8.dp, RoundedCornerShape(22.dp))
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Badge Skor +10 ⭐
                Box(
                    modifier = Modifier
                        .background(SkorKuning, RoundedCornerShape(14.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Text(
                        text       = "+10 ⭐",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 28.sp,
                        color      = Color.White
                    )
                }

                // Teks Edukasi
                Text(
                    text       = sampah.teksEdukasi,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize   = 12.5.sp,
                    color      = Color(0xFF223344),
                    textAlign  = TextAlign.Center,
                    lineHeight = 17.sp
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
                    modifier           = Modifier.size(110.dp),
                    contentScale       = ContentScale.Fit
                )

                // Tombol Biru SELANJUTNYA
                CleanUpButton(
                    text      = "SELANJUTNYA",
                    assetPath = "button/bg_btn_biru.svg",
                    onClick   = onNextClick,
                    modifier  = Modifier.fillMaxWidth(0.85f),
                    height    = 48.dp,
                    fontSize  = 16
                )
            }
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
        Box(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .background(Color(0xFFFFEEEE), RoundedCornerShape(22.dp))
                .border(3.dp, BtnMerah, RoundedCornerShape(22.dp))
                .shadow(8.dp, RoundedCornerShape(22.dp))
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {
            Column(
                modifier            = Modifier.fillMaxWidth(),
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
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = Color(0xFF333333),
                    textAlign  = TextAlign.Center
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

                CleanUpButton(
                    text      = if (isGameOver) "LIHAT HASIL" else "SELANJUTNYA",
                    assetPath = if (isGameOver) "button/bg_btn_merah.svg" else "button/bg_btn_hijau.svg",
                    onClick   = onNextClick,
                    modifier  = Modifier.fillMaxWidth(0.85f),
                    height    = 48.dp,
                    fontSize  = 16
                )
            }
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
