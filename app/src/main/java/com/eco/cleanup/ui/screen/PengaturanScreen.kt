package com.eco.cleanup.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.cleanup.ui.component.BackgroundAsset
import com.eco.cleanup.ui.component.CleanUpButton
import com.eco.cleanup.ui.component.PapanPanel
import com.eco.cleanup.ui.theme.BtnHijau
import com.eco.cleanup.ui.theme.FredokaFamily
import com.eco.cleanup.ui.theme.PlusJakartaSansFamily
import com.eco.cleanup.ui.viewmodel.PengaturanViewModel

@Composable
fun PengaturanScreen(
    viewModel : PengaturanViewModel,
    onKembali : () -> Unit
) {
    val musikOn by viewModel.musikLatar.collectAsState()
    val efekOn  by viewModel.efekSuara.collectAsState()

    BackgroundAsset(assetPath = "background/bg_beranda.png") {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            PapanPanel(
                assetPath = "papan/bg_panel_menu.svg",
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                Column(
                    modifier            = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text       = "PENGATURAN",
                        fontFamily = FredokaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        color      = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Toggle Musik Latar
                    ToggleRow(
                        label    = "Musik Latar",
                        checked  = musikOn,
                        onToggle = viewModel::setMusikLatar
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Toggle Efek Suara
                    ToggleRow(
                        label    = "Efek Suara",
                        checked  = efekOn,
                        onToggle = viewModel::setEfekSuara
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    CleanUpButton(
                        text      = "KEMBALI",
                        assetPath = "button/bg_btn_hijau.svg",
                        onClick   = onKembali,
                        modifier  = Modifier.fillMaxWidth(0.75f),
                        height    = 46.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(
    label    : String,
    checked  : Boolean,
    onToggle : (Boolean) -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = label,
            fontFamily = PlusJakartaSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 15.sp,
            color      = Color.White
        )
        Switch(
            checked         = checked,
            onCheckedChange = onToggle,
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = Color.White,
                checkedTrackColor   = BtnHijau,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

