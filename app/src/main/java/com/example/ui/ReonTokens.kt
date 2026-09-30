package com.example.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalReonExtras

/**
 * REON — Sona Minimal Android Design System Tokens
 * Source of truth for spacing, radius, sizing, typography, and tactile modifiers.
 */
object ReonSpacing {
    val unit = 4.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 20.dp
    val xl = 32.dp
    val gutter = 16.dp
    val margin = 16.dp
    val marginExpanded = 24.dp
}

object ReonRadius {
    val xs = 4.dp
    val sm = 6.dp
    val md = 8.dp
    val lg = 12.dp
    val xl = 16.dp
    val sheet = 20.dp
    val full = 9999.dp
}

object ReonSize {
    val touchTargetMin = 48.dp
    val touch = 48.dp
    val iconSm = 18.dp
    val iconMd = 24.dp
    val iconLg = 28.dp
    val trackRowHeight = 64.dp
    val trackArtRow = 48.dp
    val miniPlayerHeight = 64.dp
    val bottomNavHeight = 64.dp
    val topAppBarHeight = 56.dp
    val albumArtCard = 140.dp
    val waveformHeight = 48.dp
    val seekThumb = 12.dp
    val seekTrack = 2.dp
    val hairline = 1.dp
}

object ReonTokens {
    // Sona Calibrated Monochromatic Palette
    val Canvas = Color(0xFFF3F3F5)
    val Surface0 = Color(0xFFF3F3F5)
    val Surface1 = Color(0xFFFAFAFA)
    val Surface2 = Color(0xFFFFFFFF)
    val Surface = Color(0xFFFFFFFF)
    val Muted = Color(0xFFF4F4F5)
    val Hairline = Color(0x0F18181B)
    val HairlineBorder = Color(0xFFE4E4E7)
    val Outline = Color(0x1A18181B)
    val OutlineVariant = Color(0x0F18181B)

    // Glass & Floats
    val SurfaceGlass = Color(0xB8FFFFFF)
    val SurfaceGlassModal = Color(0xD1FAFAFA)

    // Ink & Monochromatic Hierarchy
    val InkHigh = Color(0xFF18181B)
    val InkMid = Color(0xFF3F3F46)
    val InkLow = Color(0xFF71717A)

    // Primary & Structural Accents
    val Primary = Color(0xFF18181B)
    val SoftContainer = Color(0xFFE4E4E7)
    val DeepCobalt = Color(0xFF3F3F46)
    val ActivePill = Color(0xFF18181B)
    val Hover = Color(0xFFF4F4F5)
    val Pressed = Color(0xFFE4E4E7)

    // Monochromatic Semantic Tokens
    val SuccessGreen = Color(0xFF18181B)
    val Pink = Color(0xFF18181B)

    // Architectural Monochromatic Brushes
    val ElectricGradient = androidx.compose.ui.graphics.Brush.linearGradient(
        colors = listOf(Color(0xFF18181B), Color(0xFF27272A))
    )

    val DailyMixGradient1 = androidx.compose.ui.graphics.Brush.linearGradient(
        colors = listOf(Color(0xFF27272A), Color(0xFF18181B))
    )

    val DailyMixGradient2 = androidx.compose.ui.graphics.Brush.linearGradient(
        colors = listOf(Color(0xFF3F3F46), Color(0xFF18181B))
    )

    // Text Hierarchy
    val TextPrimary = Color(0xFF18181B)
    val TextSecondary = Color(0xFF3F3F46)
    val TextTertiary = Color(0xFF71717A)

    // Radii
    val RadiusSm = ReonRadius.sm
    val RadiusDefault = ReonRadius.xs
    val RadiusMd = ReonRadius.md
    val RadiusLg = ReonRadius.lg
    val RadiusXl = ReonRadius.xl
    val RadiusFull = ReonRadius.full

    // Shapes
    val ShapeHero = RoundedCornerShape(ReonRadius.lg)
    val ShapeBento = RoundedCornerShape(ReonRadius.md)
    val ShapeInnerArt = RoundedCornerShape(ReonRadius.xs)
    val ShapeArt20 = RoundedCornerShape(ReonRadius.sm)
    val ShapeArt24 = RoundedCornerShape(ReonRadius.md)
    val ShapePill = RoundedCornerShape(ReonRadius.xs)
    val ShapeBadge = RoundedCornerShape(ReonRadius.xs)
    val ShapeDot = CircleShape

    // Margins
    val ScreenMargin = ReonSpacing.margin
    val DesktopMargin = ReonSpacing.marginExpanded
    val SectionSpacing = ReonSpacing.lg
    val CardGap = ReonSpacing.md
    val ChipGap = ReonSpacing.sm

    // Typography
    val HeadlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.6).sp,
        color = TextPrimary
    )

    val HeadlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.27).sp,
        color = TextPrimary
    )

    val TitleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.14).sp,
        color = TextPrimary
    )

    val BodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp,
        color = TextPrimary
    )

    val BodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.07).sp,
        color = TextSecondary
    )

    val BodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = TextTertiary
    )

    val LabelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        color = TextPrimary
    )

    val LabelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.12.sp,
        color = TextPrimary
    )

    val LabelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.22.sp,
        color = TextTertiary
    )

    val LabelMono = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = (-0.11).sp,
        fontFeatureSettings = "tnum",
        color = TextTertiary
    )

    val DurationText = LabelMono
}

/**
 * REON tactile button and card press: scale 0.98 over 100ms with bounded ripple.
 */
fun Modifier.reonCardPress(
    scaleDown: Float = 0.98f,
    onClick: () -> Unit = {}
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "reon_card_scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true, color = Color(0x18181B).copy(alpha = 0.08f)),
            onClick = onClick
        )
}

/**
 * Hairline framing + soft ambient elevation (Zero shadow on OLED as required by spec).
 */
fun Modifier.reonCardShadow(
    shape: RoundedCornerShape = RoundedCornerShape(ReonRadius.md),
    isActive: Boolean = false,
    elevation: Dp = if (isActive) 6.dp else 2.dp
): Modifier = composed {
    val isOled = LocalReonExtras.current.isOled
    val effectiveElevation = if (isOled) 0.dp else elevation
    val hairlineColor = LocalReonExtras.current.hairline

    this
        .shadow(
            elevation = effectiveElevation,
            shape = shape,
            ambientColor = Color(0x0A000000),
            spotColor = Color(0x1018181B)
        )
        .border(
            width = 1.dp,
            color = if (isActive) Color(0x2918181B) else hairlineColor,
            shape = shape
        )
}
