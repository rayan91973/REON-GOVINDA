package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * REON — Album Details Screen (Monolith Sessions)
 * Pixel-perfect match to user's uploaded reference UI image (Image 1):
 * - Top header with brand square + "Library", Tune, circular Avatar
 * - Navigation row: Back (<), SYNCED pill button, More (...)
 * - Brutalist Hero Artwork with "MASTER • BIT-PERFECT" badge
 * - "● STUDIO ALBUM • 24-BIT / 96kHz FLAC" overline capsule
 * - "Monolith Sessions" title, artist row with verified badge, metadata line
 * - Play, Shuffle, Heart, Share transport row
 * - "TRACKLIST 12" and "LOSSLESS AUDIO PIPELINE" header
 * - 12 numbered tracks with bitrate badges & duration
 * - "Studio Liner Notes" facility report bento card with engineering specs
 * - "More by Solaris & Kaelen" discography cards
 */
@Composable
fun AlbumScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onLikeToggle: () -> Unit = {},
    onDownloadToggle: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onPlayAllClick: () -> Unit = {},
    onMoreOptionsClick: () -> Unit = {},
    onShowToast: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    val albumTracks = remember { sampleMonolithAlbumTracks() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_album_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar (< ■ Library | Tune, Avatar >)
            item(key = "album_top_bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(onSurface, RoundedCornerShape(2.dp))
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Library",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 18.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onShowToast("Filters") },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = "Filters",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(onSurface)
                                .clickable { onShowToast("User Profile") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 2. Navigation Actions Row (< Back | ✓ SYNCED, More ... >)
            item(key = "album_nav_actions") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // SYNCED Pill Button
                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onDownloadToggle() }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = onSurface,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "SYNCED",
                                    style = ReonTokens.LabelMono,
                                    color = onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // More options button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onMoreOptionsClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreHoriz,
                                contentDescription = "More Options",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 3. Hero Artwork Section (Tall Concrete Monolith + "MASTER • BIT-PERFECT" badge)
            item(key = "album_artwork_hero") {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
                    ) {
                        TrackArtImage(
                            url = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&q=80",
                            contentDescription = "Monolith Sessions",
                            modifier = Modifier.fillMaxSize()
                        )

                        // Bottom-Left "MASTER • BIT-PERFECT" Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xE6000000))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "MASTER • BIT-PERFECT",
                                style = ReonTokens.LabelMono,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // "● STUDIO ALBUM • 24-BIT / 96kHz FLAC" Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(onSurface)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "STUDIO ALBUM • 24-BIT / 96kHz FLAC",
                                style = ReonTokens.LabelMono,
                                color = onSurfaceMuted,
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Title
                    Text(
                        text = "Monolith Sessions",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(6.dp))

                    // Artist Row with avatar & verified badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(onSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.fillMaxSize().padding(2.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Solaris & Kaelen",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 15.sp
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = "Verified",
                            tint = onSurface,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    // Subtitle Metadata Line
                    Text(
                        text = "OCT 2024  •  12 TRACKS  •  54 MIN  •  HYPERION SOUND",
                        style = ReonTokens.LabelMono,
                        color = onSurfaceMuted,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(16.dp))

                    // 4. Action Buttons Row (Play, Shuffle, Heart, Share)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Solid Black PLAY Button
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(primaryColor)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = onPrimaryColor.copy(alpha = 0.2f)),
                                    onClick = onPlayAllClick
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = "Play",
                                    tint = onPrimaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "PLAY",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onPrimaryColor,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // Shuffle Button
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShuffleClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Shuffle,
                                    contentDescription = "Shuffle",
                                    tint = onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "SHUFFLE",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    fontSize = 13.5.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // Heart Button
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onLikeToggle() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = "Like",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Share Button
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShareClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.IosShare,
                                contentDescription = "Share",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // 5. Tracklist Header (TRACKLIST 12 | LOSSLESS AUDIO PIPELINE)
            item(key = "album_tracklist_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TRACKLIST",
                            style = ReonTokens.LabelMono,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "12",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onShowToast("Lossless Audio Pipeline") }
                    ) {
                        Text(
                            text = "LOSSLESS AUDIO PIPELINE",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Rounded.DensityMedium,
                            contentDescription = null,
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            // 6. 12 Numbered Track Rows
            itemsIndexed(albumTracks, key = { index, track -> "alb_trk_${track.id}_$index" }) { index, track ->
                val isPlaying = index == 0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 2.dp)
                        .clickable { onTrackSelect(track) }
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Track Number / Soundwave
                    Box(
                        modifier = Modifier.size(28.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (isPlaying) {
                            Icon(
                                imageVector = Icons.Rounded.GraphicEq,
                                contentDescription = "Playing",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = String.format("%02d", index + 1),
                                style = ReonTokens.LabelMono,
                                fontSize = 12.sp,
                                color = onSurfaceMuted
                            )
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    // Track Title + Badge & Artist
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = track.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isPlaying) FontWeight.Bold else FontWeight.SemiBold,
                                color = onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.5.sp,
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            if (track.badge.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (track.badge == "HI-RES") onSurface else surfaceHigh)
                                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = track.badge,
                                        style = ReonTokens.LabelMono,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (track.badge == "HI-RES") MaterialTheme.colorScheme.surface else onSurfaceMuted
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = track.artist,
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp
                        )
                    }

                    // Duration
                    Text(
                        text = track.duration,
                        style = ReonTokens.LabelMono,
                        fontSize = 12.sp,
                        color = onSurfaceMuted
                    )

                    Spacer(Modifier.width(6.dp))

                    // Options Menu (⋮)
                    IconButton(
                        onClick = { onShowToast("Options for ${track.title}") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "Options",
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 7. "Studio Liner Notes" Section
            item(key = "studio_liner_notes") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(cardBackground)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            // Header: Studio Liner Notes | FACILITY REPORT
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Studio Liner Notes",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    fontSize = 17.sp
                                )

                                Text(
                                    text = "FACILITY REPORT",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 10.5.sp,
                                    color = onSurfaceMuted,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = "Captured across a 14-day continuous residency at Klangwerk Studios, Berlin. Composed exclusively with restored analogue modular synthesizer arrays and recorded to custom high-tolerance tape head amplifiers. Mastered specifically for REON Acoustics using the 24-bit/96kHz bit-perfect pipeline to preserve acoustic transient authority.",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurface,
                                lineHeight = 19.sp,
                                fontSize = 12.5.sp
                            )

                            Spacer(Modifier.height(14.dp))

                            // 2-Column Specs Grid
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh)
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("MASTERING ENGINEER", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                            Spacer(Modifier.height(2.dp))
                                            Text("E. Lindqvist", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = onSurface, fontSize = 12.sp)
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("SAMPLING DEPTH", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                            Spacer(Modifier.height(2.dp))
                                            Text("24-Bit / 96.0 kHz", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = onSurface, fontSize = 12.sp)
                                        }
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("CONSOLE", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                            Spacer(Modifier.height(2.dp))
                                            Text("Neve 8068 Custom", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = onSurface, fontSize = 12.sp)
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("MONITORING", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                            Spacer(Modifier.height(2.dp))
                                            Text("REON Ultra-Nearfield", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = onSurface, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "© 2024 Hyperion Sound under exclusive license to REON Acoustics.\nAll rights reserved. Unauthorized reproduction or re-sampling prohibited.",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceMuted,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // 8. "More by Solaris & Kaelen" Section
            item(key = "more_by_artist") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "More by Solaris & Kaelen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 17.sp
                        )

                        Text(
                            text = "DISCOGRAPHY",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Album 1: Resonance EP
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onShowToast("Opening Resonance EP") }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.art_refractions),
                                    contentDescription = "Resonance EP",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Resonance EP",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(1.dp))
                            Text(
                                text = "2023 • 5 TRACKS",
                                style = ReonTokens.LabelMono,
                                color = onSurfaceMuted,
                                fontSize = 11.sp
                            )
                        }

                        // Album 2: Nocturne Trance
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onShowToast("Opening Nocturne Trance") }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.art_refractions),
                                    contentDescription = "Nocturne Trance",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Nocturne Trance",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(1.dp))
                            Text(
                                text = "2022 • 8 TRACKS",
                                style = ReonTokens.LabelMono,
                                color = onSurfaceMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun sampleMonolithAlbumTracks(): List<TrackItem> {
    return listOf(
        TrackItem("alb_1", "Midnight City Lights", "Solaris & Kaelen", "Monolith Sessions", "4:18", badge = "HI-RES", isLiked = true, isPlaying = true, artSeed = 1),
        TrackItem("alb_2", "Concrete Horizons", "Solaris & Kaelen", "Monolith Sessions", "3:52", badge = "24-BIT", isLiked = false, artSeed = 2),
        TrackItem("alb_3", "Obsidian Echoes", "Solaris & Kaelen", "Monolith Sessions", "3:40", badge = "24-BIT", isLiked = true, artSeed = 3),
        TrackItem("alb_4", "Monolith Prelude", "Solaris & Kaelen", "Monolith Sessions", "2:15", isLiked = false, artSeed = 4),
        TrackItem("alb_5", "Structural Decay", "Solaris & Kaelen", "Monolith Sessions", "5:04", badge = "HI-RES", isLiked = false, artSeed = 5),
        TrackItem("alb_6", "Subatomic Drift", "Solaris & Kaelen", "Monolith Sessions", "4:35", isLiked = false, artSeed = 6),
        TrackItem("alb_7", "Resonance Overdrive", "Solaris & Kaelen", "Monolith Sessions", "4:22", badge = "24-BIT", isLiked = true, artSeed = 7),
        TrackItem("alb_8", "Linear Motion", "Solaris & Kaelen", "Monolith Sessions", "3:48", isLiked = false, artSeed = 8),
        TrackItem("alb_9", "Architectural Lows", "Solaris & Kaelen", "Monolith Sessions", "5:08", badge = "HI-RES", isLiked = true, artSeed = 9),
        TrackItem("alb_10", "Echo Chamber IV", "Solaris & Kaelen", "Monolith Sessions", "4:12", isLiked = false, artSeed = 10),
        TrackItem("alb_11", "Stockholm Rain", "Solaris & Kaelen", "Monolith Sessions", "3:55", badge = "24-BIT", isLiked = true, artSeed = 11),
        TrackItem("alb_12", "Nocturne Descent", "Solaris & Kaelen", "Monolith Sessions", "5:10", isLiked = false, artSeed = 12)
    )
}
