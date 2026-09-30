package com.example.ui

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GraphicEq
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * REON — Liked Songs Screen (Bit-Perfect Repository)
 * Pixel-perfect match to user's uploaded reference UI image (Image 2):
 * - Top header with brand square + "Library", Tune, circular Avatar
 * - Navigation row: Back (<), ↓ OFFLINE pill button, More (...)
 * - Dual Hero Section: Square Black Vault Card ("VAULT 01", Heart, "FLAC/DSD 96kHz") on left,
 *   "BIT-PERFECT REPOSITORY", "Liked Songs", "148 TRACKS • 9H 42M • 24-BIT MASTER" on right
 * - "PLAY ALL" solid primary button & "SHUFFLE" button
 * - "Find in Liked Songs (title, artist, format)..." search bar
 * - Filter pills: "All (148)", "Downloaded", "Masters (24-bit)" & "DATE ADDED ↓"
 * - Tracklist with numbers / soundwaves, badges (HI-RES, 24-BIT, DSD, LOSSLESS), heart icons, durations, and three-dots
 * - "● END OF LOCAL CACHE" footer
 */
@Composable
fun LikedSongsScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onPlayAllClick: () -> Unit = {},
    onToggleLike: (TrackItem) -> Unit = {},
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

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All (148)") }
    val filters = listOf("All (148)", "Downloaded", "Masters (24-bit)")

    val rawLikedTracks = remember { sampleLikedSongsList() }
    val likedTracks = remember(rawLikedTracks, searchQuery) {
        if (searchQuery.isBlank()) rawLikedTracks
        else rawLikedTracks.filter { it.title.contains(searchQuery, ignoreCase = true) || it.artist.contains(searchQuery, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_liked_songs_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp)
        ) {
            // 1. Top Bar (< ■ Library | Tune, Avatar >)
            item(key = "liked_top_bar") {
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

            // 2. Navigation Row (< Back | ↓ OFFLINE, More ... >)
            item(key = "liked_nav_row") {
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
                        // OFFLINE Pill
                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShowToast("Offline storage enabled") }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Download,
                                    contentDescription = null,
                                    tint = onSurface,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "OFFLINE",
                                    style = ReonTokens.LabelMono,
                                    color = onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // More Options
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShowToast("Liked songs settings") },
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

            // 3. Hero Header Section (Square Black Vault Card + Title Details)
            item(key = "liked_hero_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Square Black Vault Card
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF141416))
                            .padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "VAULT 01",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FavoriteBorder,
                                    contentDescription = "Liked",
                                    tint = Color.White,
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "FLAC/DSD",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 8.5.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "96kHz",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 8.5.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    // Title & Description Column
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(onSurface, RoundedCornerShape(1.dp))
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "BIT-PERFECT REPOSITORY",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Liked Songs",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 26.sp
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = "Your curated master recordings",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            fontSize = 12.5.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "148 TRACKS  •  9H 42M  •",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(surfaceHigh)
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(3.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "24-BIT MASTER",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 9.sp,
                                    color = onSurfaceMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 4. Transport Buttons (PLAY ALL & SHUFFLE)
            item(key = "liked_transport_row") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // PLAY ALL Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
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
                                contentDescription = "Play All",
                                tint = onPrimaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "PLAY ALL",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = onPrimaryColor,
                                fontSize = 13.5.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // SHUFFLE Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                            .clickable { onShowToast("Shuffling Liked Songs") },
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
                }
            }

            // 5. Search Bar Input Field
            item(key = "liked_search_bar") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Find in Liked Songs (title, artist, format)...",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceMuted,
                                fontSize = 13.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(color = onSurface, fontSize = 13.sp, fontWeight = FontWeight.Medium),
                            cursorBrush = SolidColor(onSurface),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // 6. Filter Chips & Sort Order
            item(key = "liked_filter_row") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        filters.forEach { filter ->
                            val isSelected = filter == selectedFilter
                            Box(
                                modifier = Modifier
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(if (isSelected) primaryColor else surfaceHigh)
                                    .border(
                                        ReonSize.hairline,
                                        if (isSelected) primaryColor else hairlineColor,
                                        RoundedCornerShape(15.dp)
                                    )
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filter,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) onPrimaryColor else onSurface,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onShowToast("Sorted by Date Added") }
                    ) {
                        Text(
                            text = "DATE ADDED",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted
                        )
                        Spacer(Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Rounded.ArrowDownward,
                            contentDescription = null,
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            // 7. Tracklist Rows
            itemsIndexed(likedTracks, key = { index, track -> "lkd_${track.id}_$index" }) { index, track ->
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

                    // Title + Badge & Artist
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

                    Spacer(Modifier.width(8.dp))

                    // Heart Icon (Filled)
                    IconButton(
                        onClick = { onToggleLike(track) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = "Liked",
                            tint = onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Options Menu (⋮)
                    IconButton(
                        onClick = { onShowToast("Options for ${track.title}") },
                        modifier = Modifier.size(32.dp)
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

            // 8. "END OF LOCAL CACHE" Footer
            item(key = "liked_footer") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(onSurfaceMuted)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "END OF LOCAL CACHE",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "All files encoded at Native 24-bit / 96–192kHz PCM or 1-bit DSD",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceMuted,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun sampleLikedSongsList(): List<TrackItem> {
    return listOf(
        TrackItem("lk_1", "Midnight City Lights", "Solaris & Kaelen", "Monolith Sessions", "4:18", badge = "HI-RES", isLiked = true, isPlaying = true, artSeed = 1),
        TrackItem("lk_2", "Neon Horizons", "Aura Sound", "Kinetic Resonance", "5:14", badge = "24-BIT", isLiked = true, artSeed = 2),
        TrackItem("lk_3", "Subtle Echoes", "Mirage Architecture", "Spatial Series", "3:52", badge = "DSD", isLiked = true, artSeed = 3),
        TrackItem("lk_4", "Obsidian Echoes", "Solaris & Kaelen", "Monolith Sessions", "3:40", badge = "24-BIT", isLiked = true, artSeed = 4),
        TrackItem("lk_5", "Crystalline Dispersion", "Kaelen Solo Archive", "Refractions", "4:47", badge = "LOSSLESS", isLiked = true, artSeed = 5),
        TrackItem("lk_6", "Nocturne Trance Vol. IV", "Solaris & Kaelen feat. Aura", "Nocturne", "6:12", badge = "HI-RES", isLiked = true, artSeed = 6),
        TrackItem("lk_7", "Architectural Lows", "Deep Architecture", "Structures", "5:08", badge = "24-BIT", isLiked = true, artSeed = 7),
        TrackItem("lk_8", "Velvet Cascade", "Aura & The Chamber Ensemble", "Acoustic Vault", "4:20", badge = "DSD", isLiked = true, artSeed = 8),
        TrackItem("lk_9", "Subatomic Drift", "Mirage", "Monolith Sessions", "4:35", badge = "LOSSLESS", isLiked = true, artSeed = 9)
    )
}
