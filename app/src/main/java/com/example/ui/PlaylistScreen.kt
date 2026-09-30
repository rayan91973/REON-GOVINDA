package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FileDownloadDone
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
 * REON — Playlist Screen
 * Pixel-perfect match to user's uploaded reference UI mockup (Image 1):
 * - Top header with brand square + "Library", Tune and Avatar
 * - Navigation row (< Back button, Download, More ... buttons)
 * - Large Brutalist Artwork with "24-BIT" badge
 * - "PRIVATE • REON CURATION" overline capsule
 * - "Late Night Resonance" bold title, subtitle & description
 * - Primary solid Play button, Shuffle, Heart, Share actions
 * - "Filter in playlist..." search input & "Custom order" sort
 * - Stack of high-res lossless track cards
 */
@Composable
fun PlaylistScreen(
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

    var filterQuery by remember { mutableStateOf("") }

    val rawTracks = remember(state.trendingList) {
        if (state.trendingList.isNotEmpty()) state.trendingList else samplePlaylistTrackList()
    }

    val playlistTracks = remember(rawTracks, filterQuery) {
        if (filterQuery.isBlank()) rawTracks
        else rawTracks.filter { it.title.contains(filterQuery, ignoreCase = true) || it.artist.contains(filterQuery, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_playlist_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar (< ■ Library | Tune, Avatar >)
            item(key = "playlist_top_bar") {
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

            // 2. Navigation Actions Row (< Back | Download, More ... >)
            item(key = "playlist_nav_actions") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button in soft container
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
                        // Download button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onDownloadToggle() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (state.isPlaylistDownloaded) Icons.Rounded.FileDownloadDone else Icons.Rounded.Download,
                                contentDescription = "Download Playlist",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
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

            // 3. Centered Large Artwork Hero Section with "24-BIT" badge
            item(key = "playlist_artwork_hero") {
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
                        Image(
                            painter = painterResource(R.drawable.art_refractions),
                            contentDescription = state.activePlaylistTitle,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Bottom-Right "24-BIT" Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xE6000000))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "24-BIT",
                                style = ReonTokens.LabelMono,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // "PRIVATE • REON CURATION" Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PRIVATE  •  REON CURATION",
                            style = ReonTokens.LabelMono,
                            color = onSurfaceMuted,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.5.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Playlist Title
                    Text(
                        text = state.activePlaylistTitle.ifEmpty { "Late Night Resonance" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(4.dp))

                    // Playlist Subtitle
                    Text(
                        text = "By You • 24 tracks • 1 hr 48 min",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceMuted,
                        fontSize = 12.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    // Playlist Description
                    Text(
                        text = "Deep atmospheric synths, midnight downtempo, and hypnotic progressive grooves for late night sessions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // 4. Action Buttons Row (Play, Shuffle, Heart, Share)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Solid Black "Play" Primary Button
                        Box(
                            modifier = Modifier
                                .weight(2.2f)
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
                                    text = "Play",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onPrimaryColor,
                                    fontSize = 14.5.sp
                                )
                            }
                        }

                        // Shuffle Button
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShuffleClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Shuffle,
                                contentDescription = "Shuffle",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
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
                                imageVector = if (state.isPlaylistLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (state.isPlaylistLiked) onSurface else onSurfaceMuted,
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

                    Spacer(Modifier.height(16.dp))

                    // 5. Search & Filter Bar ("Filter in playlist..." & "Custom order")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Search input field
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(cardBackground)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Search",
                                    tint = onSurfaceMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                if (filterQuery.isEmpty()) {
                                    Text(
                                        text = "Filter in playlist...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = onSurfaceMuted,
                                        fontSize = 13.sp
                                    )
                                }
                                BasicTextField(
                                    value = filterQuery,
                                    onValueChange = { filterQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(color = onSurface, fontSize = 13.sp, fontWeight = FontWeight.Medium),
                                    cursorBrush = SolidColor(onSurface),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        // Right: "Custom order" button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .height(38.dp)
                                .clickable { onShowToast("Sort: Custom order") }
                                .padding(horizontal = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DensityMedium,
                                contentDescription = "Sort order",
                                tint = onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Custom order",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = onSurface,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }
            }

            // 6. Tracklist Items
            itemsIndexed(playlistTracks, key = { index, track -> "pl_${track.id}_$index" }) { index, track ->
                val isPlaying = state.currentTrack.id == track.id && state.currentTrack.isPlaying

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPlaying) surfaceHigh else cardBackground)
                        .border(
                            ReonSize.hairline,
                            if (isPlaying) onSurface.copy(alpha = 0.2f) else hairlineColor,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onTrackSelect(track) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 44dp Artwork
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(surfaceHigh)
                    ) {
                        TrackArtImage(
                            url = getArtUrlForSeed(track.artSeed),
                            contentDescription = track.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Title & Artist + Badge
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
                                        .background(surfaceHigh)
                                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = track.badge,
                                        style = ReonTokens.LabelMono,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = onSurfaceMuted
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
                            fontSize = 12.5.sp
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

                    // More Options (⋮)
                    IconButton(
                        onClick = { onShowToast("Track options: ${track.title}") },
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
        }
    }
}

private fun samplePlaylistTrackList(): List<TrackItem> {
    return listOf(
        TrackItem("pl_1", "Midnight City Lights", "Solaris & Kaelen", "Late Night Resonance", "4:18", badge = "HI-RES", isLiked = true, isPlaying = true, artSeed = 1),
        TrackItem("pl_2", "Neon Horizons", "Aura Sound", "Late Night Resonance", "5:14", isLiked = true, artSeed = 2),
        TrackItem("pl_3", "Subtle Echoes", "Mirage Architecture", "Late Night Resonance", "3:52", isLiked = true, artSeed = 3),
        TrackItem("pl_4", "Crystalline Dispersion", "Kaelen Solo Archive", "Late Night Resonance", "4:47", badge = "LOSSLESS", isLiked = true, artSeed = 4),
        TrackItem("pl_5", "Obsidian Echoes", "Solaris", "Late Night Resonance", "3:40", isLiked = true, artSeed = 5),
        TrackItem("pl_6", "Nocturne Trance Vol. IV", "Solaris & Kaelen feat. Aura", "Late Night Resonance", "6:12", isLiked = false, artSeed = 6),
        TrackItem("pl_7", "Architectural Lows", "Deep Architecture", "Late Night Resonance", "5:08", isLiked = true, artSeed = 7),
        TrackItem("pl_8", "Subatomic Drift", "Mirage", "Late Night Resonance", "4:35", isLiked = false, artSeed = 8)
    )
}

private fun getArtUrlForSeed(seed: Int): String {
    return when (seed % 6) {
        1 -> "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?w=300&q=80"
        2 -> "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=300&q=80"
        3 -> "https://images.unsplash.com/photo-1448375240586-882707db888b?w=300&q=80"
        4 -> "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&q=80"
        5 -> "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&q=80"
        else -> "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=300&q=80"
    }
}
