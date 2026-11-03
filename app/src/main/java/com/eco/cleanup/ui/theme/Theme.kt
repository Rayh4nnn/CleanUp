package com.eco.cleanup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CleanUpColorScheme = lightColorScheme(
    primary       = BtnHijau,
    onPrimary     = TextOnDark,
    secondary     = BtnBiru,
    onSecondary   = TextOnDark,
    tertiary      = OrangePanel,
    onTertiary    = TextOnDark,
    background    = BgSplash,
    onBackground  = TextPrimary,
    surface       = OrangePanel,
    onSurface     = TextPrimary,
    error         = BtnMerah,
    onError       = TextOnDark,
)

@Composable
fun CleanUpTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CleanUpColorScheme,
        typography  = Typography,
        content     = content
    )
}
