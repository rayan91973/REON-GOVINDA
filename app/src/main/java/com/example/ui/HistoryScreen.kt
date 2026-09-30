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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Tune
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

data class HistoryLogItem(
    val id: String,
    val title: String,
    val artist: String,
    val formatAndTime: String,
    val badge: String = "",
    val isLiked: Boolean = false,
    val artSeed: Int = 1
)

data class HistoryLogGroup(
    val groupTitle: String,
    val groupRightTag: String,
    val items: List<HistoryLogItem>
)

/**
 * REON — Listening History Screen (Acoustic Log)
 * Pixel-perfect match to user's uploaded reference UI image (Image 3):
 * - Top header with brand square + "Library", Tune, circular Avatar
 * - Sub-header: Back (<), "Listening History" + "LOG.REON.SESSION_092", "Clear" (🗑)
 * - Telemetry Hero Bento Card: "● TODAY ACOUSTIC LOG", "3h 14m monitored", "Replay Queue" button,
 *   Bit-Perfect Stream 96 kHz / 24b pass-through banner
 * - Grouped history sections:
 *   - "TODAY • 4 TRACKS" (ACTIVE PROTOCOL)
 *   - "YESTERDAY — 5 HRS 22 MIN" (ARCHIVE)
 *   - "OCTOBER 24" (STORED TELEMETRY)
 */
@Composable
fun HistoryScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onClearHistoryClick: () -> Unit = {},
    onToggleLike: (String) -> Unit = {},
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

    val historyGroups = remember { sampleHistoryGroups() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_history_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp)
        ) {
            // 1. Top Bar (< ■ Library | Tune, Avatar >)
            item(key = "hist_top_bar") {
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

            // 2. Sub-header Navigation Row (< Back | Listening History / LOG.REON.SESSION_092 | Clear)
            item(key = "hist_sub_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Listening History",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "LOG.REON.SESSION_092",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Clear Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp))
                            .clickable {
                                onClearHistoryClick()
                                onShowToast("History cleared")
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = null,
                                tint = onSurfaceMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Clear",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = onSurface,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }
            }

            // 3. Telemetry Hero Card ("3h 14m monitored")
            item(key = "hist_telemetry_hero") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Top row: ● TODAY ACOUSTIC LOG | ↺ Replay Queue button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(onSurface)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "TODAY ACOUSTIC LOG",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = onSurfaceMuted,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            // Solid black Replay Queue button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(primaryColor)
                                    .clickable { onShowToast("Replaying Queue") }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.Replay,
                                        contentDescription = null,
                                        tint = onPrimaryColor,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = "Replay Queue",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = onPrimaryColor,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // Large Headline: 3h 14m monitored
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "3h 14m",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 28.sp
                            )
                            Text(
                                text = "monitored",
                                style = MaterialTheme.typography.bodyMedium,
                                color = onSurfaceMuted,
                                fontSize = 13.5.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Bit-Perfect Stream Sub-card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = onSurface,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "100% BIT-PERFECT STREAM",
                                            style = ReonTokens.LabelMono,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = onSurface
                                        )
                                        Text(
                                            text = "DIRECT DAC PASS-THROUGH • ZERO RESAMP...",
                                            style = ReonTokens.LabelMono,
                                            fontSize = 9.sp,
                                            color = onSurfaceMuted
                                        )
                                    }
                                }

                                Text(
                                    text = "96 kHz / 24b",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface
                                )
                            }
                        }
                    }
                }
            }

            // 4. Grouped History Timeline Items
            historyGroups.forEach { group ->
                item(key = "hist_header_${group.groupTitle}") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = ReonSpacing.margin,
                                end = ReonSpacing.margin,
                                top = 14.dp,
                                bottom = 6.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.groupTitle,
                            style = ReonTokens.LabelMono,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = group.groupRightTag,
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                items(group.items, key = { it.id }) { track ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ReonSpacing.margin, vertical = 2.dp)
                            .clickable {
                                onTrackSelect(
                                    TrackItem(
                                        id = track.id,
                                        title = track.title,
                                        artist = track.artist,
                                        album = "History",
                                        duration = "04:18",
                                        artSeed = track.artSeed
                                    )
                                )
                            }
                            .padding(horizontal = 4.dp, vertical = 8.dp),
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

                        // Title + Badge & Format/Time
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = track.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
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
                                            .background(primaryColor)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = track.badge,
                                            style = ReonTokens.LabelMono,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = onPrimaryColor
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(2.dp))

                            Text(
                                text = track.artist,
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.5.sp
                            )

                            Spacer(Modifier.height(2.dp))

                            Text(
                                text = track.formatAndTime,
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted
                            )
                        }

                        // Heart Button
                        IconButton(
                            onClick = { onToggleLike(track.id) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (track.isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                contentDescription = "Like",
                                tint = onSurfaceMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

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
            }
        }
    }
}

private fun sampleHistoryGroups(): List<HistoryLogGroup> {
    return listOf(
        HistoryLogGroup(
            groupTitle = "TODAY • 4 TRACKS",
            groupRightTag = "ACTIVE PROTOCOL",
            items = listOf(
                HistoryLogItem("h_1", "Midnight City Lights", "Solaris & Kaelen", "24-BIT / 96kHz FLAC • 12m ago", badge = "NOW", isLiked = false, artSeed = 1),
                HistoryLogItem("h_2", "Neon Horizons", "Aura Sound", "DSD 11.2MHz • 42m ago", isLiked = false, artSeed = 2),
                HistoryLogItem("h_3", "Subtle Echoes", "Mirage Architecture", "24-BIT / 192kHz • 1h ago", isLiked = false, artSeed = 3),
                HistoryLogItem("h_4", "Obsidian Echoes", "Solaris", "FLAC LOSSLESS • 2h ago", isLiked = false, artSeed = 4)
            )
        ),
        HistoryLogGroup(
            groupTitle = "YESTERDAY — 5 HRS 22 MIN",
            groupRightTag = "ARCHIVE",
            items = listOf(
                HistoryLogItem("h_5", "Nocturne Trance Vol. IV", "Solaris & Kaelen feat. Aura", "24-BIT / 96kHz • 11:45 PM", isLiked = false, artSeed = 6),
                HistoryLogItem("h_6", "Crystalline Dispersion", "Kaelen Solo Archive", "WAV 32-BIT FLOAT • 10:30 PM", isLiked = false, artSeed = 5),
                HistoryLogItem("h_7", "Architectural Lows", "Deep Architecture", "24-BIT / 48kHz • 9:15 PM", isLiked = false, artSeed = 7),
                HistoryLogItem("h_8", "Velvet Cascade", "Aura & The Chamber Ensemble", "DSD 5.6MHz • 8:04 PM", isLiked = false, artSeed = 8)
            )
        ),
        HistoryLogGroup(
            groupTitle = "OCTOBER 24",
            groupRightTag = "STORED TELEMETRY",
            items = listOf(
                HistoryLogItem("h_9", "Monolith Prelude", "Solaris & Kaelen", "24-BIT MASTER • Oct 24", isLiked = false, artSeed = 9),
                HistoryLogItem("h_10", "Subatomic Drift", "Mirage", "LOSSLESS • Oct 24", isLiked = false, artSeed = 10)
            )
        )
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
