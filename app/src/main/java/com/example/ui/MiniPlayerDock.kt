package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.reonExtras

/**
 * REON — Mini Player Dock
 * - Height: 64dp, 12dp radius, 8dp horizontal inset, hairline border.
 * - Glass surface, Layer 1 shadow (none on OLED).
 * - 40dp album art at 8dp radius.
 * - Title and artist with technical telemetry.
 * - 40dp play/pause button (12dp rounded-square, 48dp minimum touch target).
 * - 2dp progress line along the bottom edge (on-surface over surface-container-high).
 * - Swipe left/right to skip, tap or swipe up to expand Now Playing.
 */
@Composable
fun MiniPlayerDock(
    track: TrackItem,
    isPlaying: Boolean,
    isLiked: Boolean,
    onExpand: () -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
    playbackProgress: Float = 0.37f
) {
    val miniPlayerShape = RoundedCornerShape(ReonRadius.lg) // 12dp radius
    val isOled = MaterialTheme.reonExtras.isOled
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val accentColor = MaterialTheme.reonExtras.accent

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp) // 8dp horizontal inset
            .then(
                if (isOled) Modifier else Modifier.shadow(
                    elevation = 12.dp,
                    shape = miniPlayerShape,
                    ambientColor = Color(0x1A000000),
                    spotColor = Color(0x20000000)
                )
            )
            .clip(miniPlayerShape)
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, miniPlayerShape)
            .height(ReonSize.miniPlayerHeight) // 64dp
            .pointerInput(Unit) {
                var totalDragX = 0f
                var totalDragY = 0f
                detectDragGestures(
                    onDragStart = {
                        totalDragX = 0f
                        totalDragY = 0f
                    },
                    onDragEnd = {
                        if (totalDragY < -60f) {
                            onExpand()
                        } else if (totalDragX > 80f) {
                            onPrevious()
                        } else if (totalDragX < -80f) {
                            onNext()
                        }
                    },
                    onDragCancel = {
                        totalDragX = 0f
                        totalDragY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        totalDragX += dragAmount.x
                        totalDragY += dragAmount.y
                    }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
                onClick = onExpand
            )
            .testTag("floating_mini_player")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: 40dp Art (8dp radius) + Title / Artist
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
                            onClick = onExpand
                        )
                        .testTag("mini_track_info")
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(ReonRadius.md)) // 8dp radius
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(ReonRadius.md))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.art_refractions),
                            contentDescription = "Mini Album Art",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = track.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(1.dp))
                        Text(
                            text = "${track.artist} · 24-bit 96kHz FLAC",
                            style = ReonTokens.LabelMono,
                            fontSize = 11.sp,
                            color = MaterialTheme.reonExtras.onSurfaceMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Right Transport Controls: Like, Prev, Play/Pause (12dp rounded-square), Next
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Like button: 48dp touch target
                    Box(
                        modifier = Modifier
                            .size(ReonSize.touchTargetMin)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                                onClick = onLike
                            )
                            .testTag("mini_like_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (isLiked) "Favorited" else "Favorite",
                            tint = if (isLiked) MaterialTheme.colorScheme.onSurface else MaterialTheme.reonExtras.onSurfaceMuted,
                            modifier = Modifier.size(ReonSize.iconSm)
                        )
                    }

                    // Previous button: 48dp touch target
                    Box(
                        modifier = Modifier
                            .size(ReonSize.touchTargetMin)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                                onClick = onPrevious
                            )
                            .testTag("mini_prev_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SkipPrevious,
                            contentDescription = "Previous",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ReonSize.iconMd)
                        )
                    }

                    // Play/Pause button: 40dp visual, 12dp rounded-square, 48dp min touch target
                    Box(
                        modifier = Modifier
                            .size(ReonSize.touchTargetMin),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(ReonRadius.lg)) // 12dp rounded-square
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                                    onClick = onPlayPause
                                )
                                .testTag("mini_play_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Next button: 48dp touch target
                    Box(
                        modifier = Modifier
                            .size(ReonSize.touchTargetMin)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                                onClick = onNext
                            )
                            .testTag("mini_next_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SkipNext,
                            contentDescription = "Next",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ReonSize.iconMd)
                        )
                    }
                }
            }

            // 2dp Progress Line along the bottom edge: on-surface (or accent) over surface-container-high
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ReonSize.seekTrack) // 2dp
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(playbackProgress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(accentColor)
                )
            }
        }
    }
}
