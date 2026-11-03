package com.eco.cleanup.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.HatiRow
import com.eco.cleanup.ui.theme.BtnHijau
import com.eco.cleanup.ui.theme.BtnMerah
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.OrangePanel
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily
import com.eco.cleanup.ui.theme.PopupBg
import com.eco.cleanup.ui.theme.SkorKuning
import com.eco.cleanup.ui.theme.TongBiru
import com.eco.cleanup.ui.theme.TongHijau
import com.eco.cleanup.ui.theme.TongMerah
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
    val density = LocalDensity.current

    // Navigasi ke Hasil saat game selesai
    LaunchedEffect(state.isDone) {
        if (state.isDone && !state.showSalahPopup) {
            onHasil(state.isWin, state.skor, viewModel.getBintang())
        }
    }

    // Posisi tong untuk deteksi drop
    val tongPositions = remember { mutableMapOf<Int, Offset>() }
    val tongSizes     = remember { mutableMapOf<Int, Pair<Float, Float>>() }

    // Posisi drag item
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var isDragging  by remember { mutableStateOf(false) }

    BackgroundAsset(assetPath = "background/bg_permainan.png") {
        Box(modifier = Modifier.fillMaxSize()) {

            // ── HUD Atas ──────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                HatiRow(nyawa = state.nyawa)

                // Skor
                Box(
                    modifier = Modifier
                        .background(OrangePanel, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = "Skor: ${state.skor}",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = Color.White
                    )
                }

                // Tombol menu/pause
                val menuLoader  = svgImageLoader(context)
                val menuPainter = rememberAsyncImagePainter(
                    model       = ImageRequest.Builder(context)
                        .data(AssetHelper.assetUri("icon/ic_menu.svg"))
                        .build(),
                    imageLoader = menuLoader
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(OrangePanel, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter            = menuPainter,
                        contentDescription = "Menu",
                        modifier           = Modifier
                            .size(24.dp)
                            .then(Modifier.pointerInput(Unit) {
                                detectDragGestures { _, _ -> }
                            }),
                        contentScale       = ContentScale.Fit
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                }
                            }
                    )
                }
            }

            // ── Popup Edukasi (sebelum drag) ──────────────────────
            AnimatedVisibility(
                visible = state.showInfoPopup && state.currentSampah != null && !state.isDone,
                enter   = fadeIn() + scaleIn(),
                exit    = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
            ) {
                state.currentSampah?.let { sampah ->
                    InfoPopup(
                        namaItem     = sampah.namaSampah,
                        teksEdukasi  = sampah.teksEdukasi,
                        onSelanjutnya = viewModel::onInfoDismissed
                    )
                }
            }

            // ── Popup Salah (setelah drag salah) ─────────────────
            AnimatedVisibility(
                visible = state.showSalahPopup && !state.showInfoPopup,
                enter   = fadeIn() + scaleIn(),
                exit    = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
            ) {
                state.currentSampah?.let { sampah ->
                    InfoPopup(
                        namaItem     = sampah.namaSampah,
                        teksEdukasi  = sampah.teksEdukasi,
                        isSalah      = true,
                        onSelanjutnya = {
                            viewModel.onInfoDismissed()
                            // Jika game over, navigasi ke hasil
                            if (state.isDone) {
                                onHasil(false, state.skor, 0)
                            }
                        }
                    )
                }
            }

            // ── Item Sampah (draggable) ───────────────────────────
            if (!state.showInfoPopup && !state.showSalahPopup && !state.isDone) {
                state.currentSampah?.let { sampah ->
                    val imageLoader = svgImageLoader(context)
                    val painter     = rememberAsyncImagePainter(
                        model       = ImageRequest.Builder(context)
                            .data(AssetHelper.assetUri(sampah.asetGambar))
                            .build(),
                        imageLoader = imageLoader
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset {
                                IntOffset(
                                    dragOffsetX.roundToInt(),
                                    dragOffsetY.roundToInt()
                                )
                            }
                            .size(120.dp)
                            .pointerInput(sampah.idSampah) {
                                detectDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDragEnd   = {
                                        // Deteksi tong yang dituju
                                        val dropX = dragOffsetX
                                        val dropY = dragOffsetY
                                        val droppedTong = detectTong(
                                            dropX, dropY, tongPositions, tongSizes
                                        )
                                        if (droppedTong != null) {
                                            viewModel.onItemDropped(droppedTong)
                                        }
                                        dragOffsetX = 0f
                                        dragOffsetY = 0f
                                        isDragging  = false
                                    },
                                    onDragCancel = {
                                        dragOffsetX = 0f
                                        dragOffsetY = 0f
                                        isDragging  = false
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
                            painter            = painter,
                            contentDescription = sampah.namaSampah,
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Fit
                        )
                    }
                }
            }

            // ── Popup +10 ─────────────────────────────────────────
            AnimatedVisibility(
                visible  = state.showSkorPopup,
                enter    = fadeIn() + scaleIn(),
                exit     = fadeOut() + scaleOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .background(SkorKuning, RoundedCornerShape(16.dp))
                        .border(3.dp, Color.White, RoundedCornerShape(16.dp))
                        .padding(horizontal = 28.dp, vertical = 12.dp)
                ) {
                    Text(
                        text       = "+10",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 36.sp,
                        color      = Color.White
                    )
                }
            }

            // ── 3 Tong Sampah (bawah) ─────────────────────────────
            Row(
                modifier              = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment     = Alignment.Bottom
            ) {
                listOf(
                    Triple(1, TongHijau,  "Organik"),
                    Triple(2, TongBiru,   "Anorganik"),
                    Triple(3, TongMerah,  "B3")
                ).forEach { (idKategori, color, label) ->
                    TongSampah(
                        idKategori   = idKategori,
                        color        = color,
                        label        = label,
                        onPositioned = { pos, size ->
                            tongPositions[idKategori] = pos
                            tongSizes[idKategori]     = size
                        }
                    )
                }
            }

            // ── Pause Dialog ──────────────────────────────────────
            if (state.isPaused) {
                PauseDialog(
                    onLanjutkan = viewModel::onResume,
                    onUlang     = {
                        viewModel.onRestart()
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                    },
                    onKeluar    = onBackToPeta
                )
            }
        }
    }
}

// ── Info Popup ────────────────────────────────────────────────────────────────

@Composable
private fun InfoPopup(
    namaItem      : String,
    teksEdukasi   : String,
    isSalah       : Boolean = false,
    onSelanjutnya : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSalah) Color(0xFFFFEBEE) else PopupBg,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = if (isSalah) BtnMerah else OrangePanel,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isSalah) {
                Text(
                    text       = "❌ Ups, salah tong!",
                    fontFamily = FredokaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = BtnMerah
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            Text(
                text       = namaItem,
                fontFamily = FredokaFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = Color(0xFF333333),
                textAlign  = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text       = teksEdukasi,
                fontFamily = PlusJakartaSansFamily,
                fontSize   = 13.sp,
                color      = Color(0xFF555555),
                textAlign  = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            CleanUpButton(
                text      = "SELANJUTNYA",
                assetPath = "button/bg_btn_hijau.svg",
                onClick   = onSelanjutnya,
                modifier  = Modifier.fillMaxWidth(0.7f),
                height    = 42.dp,
                fontSize  = 14
            )
        }
    }
}

// ── Tong Sampah ───────────────────────────────────────────────────────────────

@Composable
private fun TongSampah(
    idKategori   : Int,
    color        : Color,
    label        : String,
    onPositioned : (Offset, Pair<Float, Float>) -> Unit
) {
    val context     = LocalContext.current
    val imageLoader = svgImageLoader(context)
    val painter     = rememberAsyncImagePainter(
        model       = ImageRequest.Builder(context)
            .data(AssetHelper.assetUri("item/tempat_sampah.svg"))
            .build(),
        imageLoader = imageLoader
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text       = label,
            fontFamily = FredokaFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 12.sp,
            color      = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                .border(2.dp, color, RoundedCornerShape(12.dp))
                .onGloballyPositioned { coords ->
                    val pos  = coords.positionInRoot()
                    val size = Pair(
                        coords.size.width.toFloat(),
                        coords.size.height.toFloat()
                    )
                    onPositioned(pos, size)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter            = painter,
                contentDescription = "Tong $label",
                modifier           = Modifier
                    .size(64.dp)
                    .padding(4.dp),
                contentScale       = ContentScale.Fit
            )
        }
    }
}

// ── Pause Dialog ──────────────────────────────────────────────────────────────

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
                .fillMaxWidth(0.8f)
                .background(OrangePanel, RoundedCornerShape(20.dp))
                .border(3.dp, Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

// ── Helper: Deteksi tong dari posisi drop ─────────────────────────────────────

private fun detectTong(
    dropX         : Float,
    dropY         : Float,
    tongPositions : Map<Int, Offset>,
    tongSizes     : Map<Int, Pair<Float, Float>>
): Int? {
    // dropX/Y adalah offset dari pusat item relatif ke posisi awal
    // Sederhana: cek tong mana yang terdekat dari arah horizontal drag
    return when {
        dropX < -150f -> 1  // Kiri → Organik (Hijau)
        dropX > 150f  -> 3  // Kanan → B3 (Merah)
        else          -> 2  // Tengah → Anorganik (Biru)
    }
}

