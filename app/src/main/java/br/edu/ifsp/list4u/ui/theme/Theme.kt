package br.edu.ifsp.list4u.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CleanColorScheme = lightColorScheme(
    primary          = AccentBlack,
    onPrimary        = White,
    background       = White,
    onBackground     = TextPrimary,
    surface          = OffWhite,
    onSurface        = TextPrimary,
    surfaceVariant   = Surface,
    onSurfaceVariant = TextSecond,
    outline          = DividerColor,
    error            = DestructRed,
    onError          = White,
)

@Composable
fun List4UTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CleanColorScheme,
        typography  = Typography,
        content     = content
    )
}