package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NowPlayingState
import com.example.ui.ReonRadius
import com.example.ui.ReonSize
import com.example.ui.ReonSpacing
import com.example.ui.ReonTokens
import com.example.ui.theme.reonExtras
import kotlin.math.abs
import kotlin.math.sin

/**
 * Precomputed pseudo-random amplitude heights normalized 0.15f..1.0f
 */
private val PRECOMPUTED_AMPLITUDES = floatArrayOf(
    0.28f, 0.45f, 0.62f, 0.78f, 0.55f, 0.88f, 0.95f, 0.72f, 0.60f, 0.82f,
    0.98f, 0.76f, 0.64f, 0.52f, 0.85f, 0.92f, 0.70f, 0.58f, 0.80f, 0.90f,
    0.68f, 0.50f, 0.75f, 0.88f, 0.65f, 0.82f, 0.94f, 0.78f, 0.60f, 0.74f,
    0.89f, 0.68f, 0.55f, 0.79f, 0.91f, 0.73f, 0.62f, 0.84f, 0.96f, 0.70f,
    0.54f, 0.67f, 0.83f, 0.92f, 0.75f, 0.61f, 0.78f, 0.87f, 0.69f, 0.56f,
    0.72f, 0.85f, 0.93f, 0.77f, 0.63f, 0.81f, 0.89f, 0.71f, 0.59f, 0.76f,
    0.90f, 0.67f, 0.53f, 0.68f, 0.82f, 0.95f, 0.74f, 0.60f, 0.77f, 0.86f
)

/**
 * REON Bit-Perfect Waveform Seek Bar
 * Meets exact specifications:
 * - Height: 48dp
 * - Bars: 2dp wide, 2dp gap, 1dp rounded ends
 * - Played: on-surface (or chosen accent)
 * - Unplayed: surface-container-high
 * - Touch thumb: 12dp circle with 1dp surface outline, appears on touch only
 * - Time codes: label-mono with tabular numbers
 * - Accessibility: semantic progress slider
 */
@Composable
fun ReonWaveformSeekBar(
    progress: Float,
    durationMs: Long,
    isPlaying: Boolean,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    elapsedText: String = "01:24",
    remainingText: String = "-02:23"
) {
    val playedColor = MaterialTheme.reonExtras.accent
    val unplayedColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val thumbOutlineColor = MaterialTheme.colorScheme.surface

    var isTouching by remember { mutableStateOf(false) }
    var touchProgress by remember { mutableFloatStateOf(progress) }

    val currentProgress by remember(isTouching, touchProgress, progress) {
        derivedStateOf { if (isTouching) touchProgress else progress }
    }

    val animatedThumbAlpha by animateFloatAsState(
        targetValue = if (isTouching) 1f else 0f,
        animationSpec = tween(140),
        label = "thumb_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                progressBarRangeInfo = ProgressBarRangeInfo(currentProgress, 0f..1f)
                setProgress { target ->
                    onSeek(target.coerceIn(0f, 1f))
                    true
                }
            }
            .testTag("waveform_seek_bar")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ReonSize.waveformHeight)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            isTouching = true
                            touchProgress = (offset.x / size.width).coerceIn(0f, 1f)
                            val success = tryAwaitRelease()
                            if (success) {
                                onSeek(touchProgress)
                            }
                            isTouching = false
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            isTouching = true
                            touchProgress = (offset.x / size.width).coerceIn(0f, 1f)
                        },
                        onDragEnd = {
                            onSeek(touchProgress)
                            isTouching = false
                        },
                        onDragCancel = {
                            isTouching = false
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            touchProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val barWidth = 2.dp.toPx()
                val barGap = 2.dp.toPx()
                val cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())

                val totalBars = (canvasWidth / (barWidth + barGap)).toInt().coerceAtLeast(10)
                val activeX = currentProgress * canvasWidth

                for (i in 0 until totalBars) {
                    val x = i * (barWidth + barGap)
                    if (x > canvasWidth) break

                    val amplitudeIndex = i % PRECOMPUTED_AMPLITUDES.size
                    val baseAmp = PRECOMPUTED_AMPLITUDES[amplitudeIndex]
                    val barHeight = (canvasHeight * (0.15f + baseAmp * 0.85f)).coerceIn(4.dp.toPx(), canvasHeight)
                    val y = (canvasHeight - barHeight) / 2f

                    val isPlayed = (x + barWidth / 2f) <= activeX
                    val color = if (isPlayed) playedColor else unplayedColor

                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = cornerRadius
                    )
                }

                // Touch Thumb: 12dp circle with 1dp surface outline (visible on touch)
                if (animatedThumbAlpha > 0.01f) {
                    val thumbRadius = 6.dp.toPx()
                    val thumbX = activeX.coerceIn(thumbRadius, canvasWidth - thumbRadius)
                    val thumbY = canvasHeight / 2f

                    // 1dp outline in surface color
                    drawCircle(
                        color = thumbOutlineColor.copy(alpha = animatedThumbAlpha),
                        radius = thumbRadius + 1.dp.toPx(),
                        center = Offset(thumbX, thumbY)
                    )
                    // Inner ink dot
                    drawCircle(
                        color = playedColor.copy(alpha = animatedThumbAlpha),
                        radius = thumbRadius,
                        center = Offset(thumbX, thumbY)
                    )
                }
            }
        }

        Spacer(Modifier.height(ReonSpacing.xs))

        // Tabular Monospace Time Codes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = elapsedText,
                style = ReonTokens.LabelMono,
                color = MaterialTheme.reonExtras.onSurfaceMuted,
                fontSize = 11.sp
            )

            Text(
                text = remainingText,
                style = ReonTokens.LabelMono,
                color = MaterialTheme.reonExtras.onSurfaceMuted,
                fontSize = 11.sp
            )
        }
    }
}

/**
 * Compact visualizer for lists and cards.
 */
@Composable
fun MiniWaveform(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 32,
    activeColor: Color = MaterialTheme.colorScheme.onSurface,
    mutedColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    progress: Float = 0.37f
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
    ) {
        val width = size.width
        val height = size.height
        val totalSpacing = (barCount - 1) * 2.dp.toPx()
        val barWidth = ((width - totalSpacing) / barCount).coerceAtLeast(1.5.dp.toPx())
        val spacing = 2.dp.toPx()

        for (i in 0 until barCount) {
            val normalizedIndex = i.toFloat() / barCount
            val amp = PRECOMPUTED_AMPLITUDES[i % PRECOMPUTED_AMPLITUDES.size]
            val barHeight = (height * (0.2f + amp * 0.8f)).coerceIn(4.dp.toPx(), height)

            val x = i * (barWidth + spacing)
            val y = (height - barHeight) / 2f
            val isPlayed = normalizedIndex <= progress

            drawRoundRect(
                color = if (isPlayed) activeColor else mutedColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
        }
    }
}

@Composable
fun WaveformBentoCard(
    state: NowPlayingState,
    onPresetSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf("Hi-Fi Pure", "Spatial 3D", "Studio Flat")

    BentoCard(
        modifier = modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.GraphicEq,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "SPECTRUM TELEMETRY",
                    style = ReonTokens.LabelMono,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.reonExtras.onSurfaceMuted
                )
            }

            // Quality Badge: 4dp radius, label-mono, 1dp outline
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(ReonRadius.xs))
                    .background(Color.Transparent)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(ReonRadius.xs))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = state.codec,
                    style = ReonTokens.LabelMono,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(ReonSpacing.md))

        MiniWaveform(
            isPlaying = state.isPlaying,
            progress = state.progress,
            activeColor = MaterialTheme.reonExtras.accent,
            mutedColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )

        Spacer(Modifier.height(ReonSpacing.md))

        // Segmented Control: 36dp height, surfaceContainerHigh track, 4dp radius
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(ReonRadius.xs))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                presets.forEach { preset ->
                    val isSelected = state.selectedEqPreset == preset
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent
                            )
                            .clickable { onPresetSelect(preset) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = preset,
                            style = ReonTokens.LabelMedium,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.reonExtras.onSurfaceMuted,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
