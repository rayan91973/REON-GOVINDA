package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Immutable
data class ReonCustomization(
    val accentColor: Color = ReonAccents[0],
    val backgroundColor: Color = LightCanvasBackgrounds[0],
    val surfaceColor: Color = Color(0xFFFAFAFA),
    val isDark: Boolean = false,
    val isOled: Boolean = false,
    val fontFamily: FontFamily = FontFamily.SansSerif,
    val fontScale: Float = 1.0f
)

val LocalReonCustomization = staticCompositionLocalOf { ReonCustomization() }

val ReonShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp), // xs: 4dp
    small = RoundedCornerShape(6.dp),      // sm: 6dp
    medium = RoundedCornerShape(8.dp),     // md: 8dp
    large = RoundedCornerShape(12.dp),     // lg: 12dp
    extraLarge = RoundedCornerShape(16.dp) // xl: 16dp
)

object ReonColors {
    val ElectricBlue = Color(0xFF18181B)
    val ElectricBlueDeep = Color(0xFF000000)
    val ElectricBlueSoft = Color(0xFFEEEEF0)
    val ElectricBlueGlow = Color(0x1018181B)
    val Background = Color(0xFFF3F3F5)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceMuted = Color(0xFFF4F4F5)
    val Border = Color(0xFFE4E4E7)
    val TextPrimary = Color(0xFF18181B)
    val TextSecondary = Color(0xFF3F3F46)
    val TextTertiary = Color(0xFF71717A)
    val Like = Color(0xFF18181B)
    val Success = Color(0xFF18181B)
}

object ReonSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 32.dp
    val cardRadius = 8.dp
    val heroRadius = 12.dp
}

@Composable
fun ReonTheme(
    themeMode: String = "LIGHT",
    accentColorIndex: Int = 0,
    backgroundThemeIndex: Int = 0,
    fontFamilyChoice: String = "SANS_SERIF",
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        "DARK", "OLED" -> true
        "LIGHT" -> false
        else -> isSystemDark
    }
    val isOled = themeMode == "OLED"

    // Raw accent from catalog
    val rawAccent = ReonAccents.getOrElse(accentColorIndex) { ReonAccents[0] }

    // Contrast adjustment rule for dark theme:
    // If dark/oled and accent is Electric Blue (#0057FF) or Emerald (#10B981), lighten by ~12%
    val effectiveAccent = if (isDark && accentColorIndex == 0) {
        Color(0xFFFAFAFA) // Graphite in dark inverts to on-surface
    } else if (isDark && accentColorIndex == 1) {
        Color(0xFF3379FF) // Lightened Electric Blue
    } else if (isDark && accentColorIndex == 4) {
        Color(0xFF34D399) // Lightened Emerald
    } else {
        rawAccent
    }

    val selectedFontFamily = when (fontFamilyChoice) {
        "INTER" -> FontFamily.Default
        "MONOSPACE" -> FontFamily.Monospace
        "SERIF" -> FontFamily.Serif
        else -> FontFamily.SansSerif
    }

    val baseScheme = when {
        isOled -> OledScheme
        isDark -> DarkScheme
        else -> LightScheme
    }

    // Canvas background undertone tinting
    val canvasBg = if (isOled) {
        Color(0xFF000000)
    } else if (isDark) {
        DarkCanvasBackgrounds.getOrElse(backgroundThemeIndex) { DarkCanvasBackgrounds[0] }
    } else {
        LightCanvasBackgrounds.getOrElse(backgroundThemeIndex) { LightCanvasBackgrounds[0] }
    }

    val resolvedColorScheme = baseScheme.copy(
        background = canvasBg
    )

    val typography = createReonTypography(
        baseFamily = selectedFontFamily,
        fontScale = fontScale
    )

    val extras = ReonExtras(
        accent = effectiveAccent,
        onSurfaceMuted = if (isDark) Color(0xFF8A8A93) else Color(0xFF71717A),
        glass = if (isDark) Color(0xB818181B) else Color(0xB8FFFFFF),
        hairline = if (isDark) Color(0x12FFFFFF) else Color(0x0F18181B),
        isOled = isOled
    )

    val customization = ReonCustomization(
        accentColor = effectiveAccent,
        backgroundColor = canvasBg,
        surfaceColor = resolvedColorScheme.surface,
        isDark = isDark,
        isOled = isOled,
        fontFamily = selectedFontFamily,
        fontScale = fontScale
    )

    CompositionLocalProvider(
        LocalReonCustomization provides customization,
        LocalReonExtras provides extras
    ) {
        MaterialTheme(
            colorScheme = resolvedColorScheme,
            typography = typography,
            shapes = ReonShapes,
            content = content
        )
    }
}

@Composable
fun ReonTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    ReonTheme(
        themeMode = if (darkTheme) "DARK" else "LIGHT",
        content = content
    )
}
