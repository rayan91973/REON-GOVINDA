package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
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
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * REON — Floating Mini Player Dock
 * Pixel-perfect match to user's uploaded mockup:
 * - Square album thumbnail on the left
 * - Active track title & artist format metadata
 * - Heart icon button
 * - Circular Pause/Play button
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
    val miniPlayerShape = RoundedCornerShape(16.dp)
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .shadow(
                elevation = 8.dp,
                shape = miniPlayerShape,
                ambientColor = Color(0x14000000),
                spotColor = Color(0x1A000000)
            )
            .clip(miniPlayerShape)
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, miniPlayerShape)
            .height(68.dp)
            .pointerInput(Unit) {
                var totalDragX = 0f
                var totalDragY = 0f
                detectDragGestures(
                    onDragStart = {
                        totalDragX = 0f
                        totalDragY = 0f
                    },
                    onDragEnd = {
                        if (totalDragY < -50f) {
                            onExpand()
                        } else if (totalDragX > 70f) {
                            onPrevious()
                        } else if (totalDragX < -70f) {
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
                indication = ripple(bounded = true, color = onSurface.copy(alpha = 0.05f)),
                onClick = onExpand
            )
            .testTag("floating_mini_player")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Square Thumbnail + Title / Subtitle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = onSurface.copy(alpha = 0.05f)),
                            onClick = onExpand
                        )
                        .testTag("mini_track_info")
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                    ) {
                        TrackArtImage(
                            url = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=300&q=80",
                            contentDescription = "Mini Album Art",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = track.title.ifEmpty { "Midnight City Lights" },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 14.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${track.artist.ifEmpty { "Solaris & Kaelen" }} • 24-BIT",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.5.sp,
                            color = onSurfaceMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Right Transport Controls: Heart + Circular Pause/Play
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Like button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = onSurface.copy(alpha = 0.08f)),
                                onClick = onLike
                            )
                            .testTag("mini_like_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (isLiked) "Favorited" else "Favorite",
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Circular Play/Pause button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = onSurface.copy(alpha = 0.15f)),
                                onClick = onPlayPause
                            )
                            .testTag("mini_play_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Subtle 2dp progress bar along bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(surfaceHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(playbackProgress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(onSurface)
                )
            }
        }
    }
}
