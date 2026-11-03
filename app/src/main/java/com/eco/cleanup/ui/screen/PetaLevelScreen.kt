package com.eco.cleanup.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import com.eco.cleanup.ui.theme.StarYellow
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

    BackgroundAsset(assetPath = "background/bg_peta_level.png") {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
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
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .background(OrangePanel, RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text       = "PETA LEVEL",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Level nodes (scroll)
            Column(
                modifier              = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment   = Alignment.CenterHorizontally,
                verticalArrangement   = Arrangement.spacedBy(24.dp)
            ) {
                levels.reversed().forEach { level ->
                    LevelNode(
                        level           = level,
                        onLevelSelected = onLevelSelected
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun LevelNode(
    level           : LevelEntity,
    onLevelSelected : (Int) -> Unit
) {
    val context     = LocalContext.current
    val imageLoader = svgImageLoader(context)

    val iconPath = when {
        !level.statusTerbuka                   -> "icon/ic_level_gembok.svg"
        level.idLevel % 2 == 0                 -> "icon/ic_level_2.svg"
        else                                   -> "icon/ic_level_1.svg"
    }

    val painter = rememberAsyncImagePainter(
        model       = ImageRequest.Builder(context)
            .data(AssetHelper.assetUri(iconPath))
            .build(),
        imageLoader = imageLoader
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nama level
        Text(
            text       = level.namaLevel,
            fontFamily = FredokaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp,
            color      = Color.White,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Bintang kecil di atas node
        if (level.bintang > 0) {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(3) { i ->
                    Text(
                        text  = if (i < level.bintang) "⭐" else "☆",
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }

        // Node lingkaran
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(if (level.statusTerbuka) BtnHijau else Color.Gray)
                .border(3.dp, Color.White, CircleShape)
                .alpha(if (level.statusTerbuka) 1f else 0.7f)
                .clickable(enabled = level.statusTerbuka) {
                    onLevelSelected(level.idLevel)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter            = painter,
                contentDescription = "Level ${level.idLevel}",
                modifier           = Modifier
                    .size(48.dp)
                    .padding(4.dp),
                contentScale       = ContentScale.Fit
            )
        }

        // Nomor level
        Text(
            text       = "${level.idLevel}",
            fontFamily = FredokaFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = Color.White
        )
    }
}

