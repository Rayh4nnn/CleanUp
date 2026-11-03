package com.eco.cleanup.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eco.cleanup.data.local.entity.LevelEntity
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.theme.BtnHijau
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.OrangePanel
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily
import com.eco.cleanup.ui.viewmodel.PetaLevelViewModel
import com.eco.cleanup.util.AssetHelper
import com.eco.cleanup.util.svgImageLoader

@Composable
fun PetaLevelScreen(
    viewModel       : PetaLevelViewModel,
    onLevelSelected : (Int) -> Unit,
    onBack          : () -> Unit
) {
    val levels by viewModel.levels.collectAsState()

    // Level configuration yang mengikuti alur jalan berliku (S-curve) di background
    // fractionX dan fractionY menentukan posisi tepat di atas jalan
    data class LevelMapConfig(
        val idLevel    : Int,
        val defaultNama: String,
        val fractionX  : Float,
        val fractionY  : Float
    )

    val mapConfigs = remember {
        listOf(
            LevelMapConfig(idLevel = 1, defaultNama = "Mudah",  fractionX = 0.44f, fractionY = 0.725f),
            LevelMapConfig(idLevel = 2, defaultNama = "Sedang", fractionX = 0.57f, fractionY = 0.565f),
            LevelMapConfig(idLevel = 3, defaultNama = "Susah",  fractionX = 0.45f, fractionY = 0.415f)
        )
    }

    BackgroundAsset(assetPath = "background/bg_peta_level.png") {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            // ── Header (Tombol Kembali & Judul Peta Level) ───────────
            Row(
                modifier          = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 32.dp, start = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Back bulat hijau
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BtnHijau)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "←",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 24.sp,
                        color      = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Banner Judul PETA LEVEL
                Box(
                    modifier = Modifier
                        .background(OrangePanel, RoundedCornerShape(14.dp))
                        .border(2.dp, Color(0xFFC46A1F), RoundedCornerShape(14.dp))
                        .padding(horizontal = 22.dp, vertical = 8.dp)
                ) {
                    Text(
                        text       = "PETA LEVEL",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 19.sp,
                        color      = Color.White
                    )
                }
            }

            // ── Node Level di Sepanjang Garis Jalan ─────────────────
            mapConfigs.forEach { config ->
                val levelData = levels.find { it.idLevel == config.idLevel }
                    ?: LevelEntity(
                        idLevel       = config.idLevel,
                        namaLevel     = config.defaultNama,
                        statusTerbuka = (config.idLevel == 1),
                        bintang       = 0,
                        skorTertinggi = 0
                    )

                val nodeWidth = 92.dp
                val nodeHeight = 125.dp

                Box(
                    modifier = Modifier
                        .offset(
                            x = screenWidth * config.fractionX - (nodeWidth / 2),
                            y = screenHeight * config.fractionY - (nodeHeight / 2)
                        )
                        .size(width = nodeWidth, height = nodeHeight),
                    contentAlignment = Alignment.Center
                ) {
                    LevelNodeOnRoad(
                        level           = levelData,
                        onLevelSelected = onLevelSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelNodeOnRoad(
    level           : LevelEntity,
    onLevelSelected : (Int) -> Unit
) {
    val context   = LocalContext.current
    val svgLoader = remember { svgImageLoader(context) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── 1. Tiga Bintang di Atas Lingkaran ─────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val isEarned = index < level.bintang
                Text(
                    text     = "★",
                    fontSize = 16.sp,
                    color    = if (isEarned) Color(0xFFFFC107) else Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // ── 2. Lingkaran Level (Hijau jika terbuka, Abu-abu jika gembok) ──
        val iconPath = when {
            !level.statusTerbuka   -> "icon/ic_level_gembok.svg"
            level.idLevel == 1     -> "icon/ic_level_1.svg"
            level.idLevel == 2     -> "icon/ic_level_2.svg"
            else                   -> null
        }

        val nodeSize = 78.dp

        if (iconPath != null) {
            val painter = rememberAsyncImagePainter(
                model       = ImageRequest.Builder(context)
                    .data(AssetHelper.assetUri(iconPath))
                    .build(),
                imageLoader = svgLoader
            )
            Box(
                modifier = Modifier
                    .size(nodeSize)
                    .clickable(enabled = level.statusTerbuka) {
                        onLevelSelected(level.idLevel)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter            = painter,
                    contentDescription = "Level ${level.idLevel}",
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Fit
                )
            }
        } else {
            // Level 3 saat terbuka
            Box(
                modifier = Modifier
                    .size(nodeSize)
                    .clip(CircleShape)
                    .background(BtnHijau)
                    .border(4.dp, Color.White, CircleShape)
                    .clickable(enabled = level.statusTerbuka) {
                        onLevelSelected(level.idLevel)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "${level.idLevel}",
                    fontFamily = FredokaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 32.sp,
                    color      = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── 3. Label Nama Level (Pill Putih di Bawah Lingkaran) ───────
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.5.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = level.namaLevel,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 11.5.sp,
                color      = Color(0xFF333333),
                textAlign  = TextAlign.Center
            )
        }
    }
}
