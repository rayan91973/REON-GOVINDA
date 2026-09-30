package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightScheme = lightColorScheme(
    background = Color(0xFFF3F3F5),
    surface = Color(0xFFFAFAFA),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F7F8),
    surfaceContainer = Color(0xFFF4F4F5),
    surfaceContainerHigh = Color(0xFFE4E4E7),
    onSurface = Color(0xFF18181B),
    onSurfaceVariant = Color(0xFF3F3F46),
    outline = Color(0x1A18181B),             // rgba(24,24,27,0.10)
    outlineVariant = Color(0x0F18181B),      // rgba(24,24,27,0.06)
    primary = Color(0xFF18181B),
    onPrimary = Color.White,
    secondaryContainer = Color(0xFFE4E4E7),
    onSecondaryContainer = Color(0xFF18181B),
    surfaceTint = Color.Transparent,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A)
)

val DarkScheme = darkColorScheme(
    background = Color(0xFF0B0B0C),
    surface = Color(0xFF111113),
    surfaceContainerLowest = Color(0xFF0B0B0C),
    surfaceContainerLow = Color(0xFF151517),
    surfaceContainer = Color(0xFF18181B),
    surfaceContainerHigh = Color(0xFF232326),
    onSurface = Color(0xFFFAFAFA),
    onSurfaceVariant = Color(0xFFD4D4D8),
    outline = Color(0x1FFFFFFF),             // rgba(255,255,255,0.12)
    outlineVariant = Color(0x12FFFFFF),      // rgba(255,255,255,0.07)
    primary = Color(0xFFFAFAFA),
    onPrimary = Color(0xFF18181B),
    secondaryContainer = Color(0xFF2A2A2E),
    onSecondaryContainer = Color(0xFFFAFAFA),
    surfaceTint = Color.Transparent,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

val OledScheme = darkColorScheme(
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = Color(0xFF0A0A0B),
    surfaceContainer = Color(0xFF111113),
    surfaceContainerHigh = Color(0xFF1A1A1D),
    onSurface = Color(0xFFFAFAFA),
    onSurfaceVariant = Color(0xFFD4D4D8),
    outline = Color(0x1FFFFFFF),
    outlineVariant = Color(0x12FFFFFF),
    primary = Color(0xFFFAFAFA),
    onPrimary = Color(0xFF18181B),
    secondaryContainer = Color(0xFF1A1A1D),
    onSecondaryContainer = Color(0xFFFAFAFA),
    surfaceTint = Color.Transparent,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Immutable
data class ReonExtras(
    val accent: Color,          // Graphite = primary; otherwise user accent
    val onSurfaceMuted: Color,  // #71717A light / #8A8A93 dark
    val glass: Color,
    val hairline: Color,
    val isOled: Boolean = false
)

val LocalReonExtras = staticCompositionLocalOf<ReonExtras> {
    ReonExtras(
        accent = Color(0xFF18181B),
        onSurfaceMuted = Color(0xFF71717A),
        glass = Color(0xB8FFFFFF),
        hairline = Color(0x0F18181B),
        isOled = false
    )
}

val androidx.compose.material3.MaterialTheme.reonExtras: ReonExtras
    @Composable
    @ReadOnlyComposable
    get() = LocalReonExtras.current

val ReonAccents = listOf(
    Color(0xFF18181B), // 0: Graphite (Default - monochrome)
    Color(0xFF0057FF), // 1: Electric Blue
    Color(0xFF00D2FF), // 2: Neon Cyan
    Color(0xFF8B5CF6), // 3: Cyber Purple
    Color(0xFF10B981), // 4: Emerald
    Color(0xFFFF4757), // 5: Sunset Coral
    Color(0xFFFF3377)  // 6: Rose Magenta
)

val AccentColors = ReonAccents

val LightCanvasBackgrounds = listOf(
    Color(0xFFF3F3F5), // 0: Default Minimal Mist
    Color(0xFFF0F3F7), // 1: Ice Blue undertone
    Color(0xFFFAFAFA), // 2: Minimal White
    Color(0xFFF2F3F5), // 3: Soft Slate
    Color(0xFFF6F4F0)  // 4: Warm Sand
)

val DarkCanvasBackgrounds = listOf(
    Color(0xFF0B0B0C), // 0: Obsidian
    Color(0xFF0C0E12), // 1: Cool Ice Blue
    Color(0xFF121214), // 2: Minimal Charcoal
    Color(0xFF141518), // 3: Soft Slate
    Color(0xFF151412)  // 4: Warm Sand
)

val LightBackgrounds = LightCanvasBackgrounds
val DarkBackgrounds = DarkCanvasBackgrounds
