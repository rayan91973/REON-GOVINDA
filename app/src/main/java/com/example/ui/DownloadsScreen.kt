package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

data class DownloadedVaultItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val formatMeta: String,
    val artSeed: Int = 1,
    val iconType: String = "image"
)

/**
 * REON — Offline Vault (Downloads Screen)
 * Pixel-perfect match to user's uploaded reference UI mockup (Image 2 & 3):
 * - Top header with brand square + "Library", Tune and Avatar
 * - "Offline Vault" NVMe headline with Refresh & Storage actions
 * - "BIT-PERFECT STORAGE BAY" with 1.42 GB bar, Lossless Verified & Smart Sync On
 * - "In Transit" active download queues (Midnight City Lights 72%, Neon Horizons 45%)
 * - "Downloaded Vault" with filter pills (All 42, Masters 24-bit, DSD/DXD, Playlists)
 * - List of downloaded masters with verified check overlays & Play triggers
 * - "VAULT SETTINGS" card with Master Download Quality, Wi-Fi only switch, and Codec Priority button
 */
@Composable
fun DownloadsScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit,
    onBackClick: () -> Unit = {},
    onDownloadAll: () -> Unit = {},
    onRemoveDownload: (String) -> Unit = {},
    onToggleOfflineMode: () -> Unit = {},
    onToggleAutoSync: () -> Unit = {},
    onToggleCellular: () -> Unit = {},
    onFilterSelect: (String) -> Unit = {},
    onQualitySelect: (String) -> Unit = {},
    onToggleQualitySelector: (Boolean) -> Unit = {},
    onShuffleAll: () -> Unit = {},
    onClearAll: () -> Unit = {},
    onOpenPlaylist: (PlaylistItem?) -> Unit = {},
    onOpenAlbum: (AlbumItem?) -> Unit = {},
    onOpenArtist: (ArtistItem?) -> Unit = {},
    onOpenLikedSongs: () -> Unit = {},
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

    var wifiOnlyEnabled by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf("All (42)") }
    val vaultFilters = listOf("All (42)", "Masters (24-bit)", "DSD / DXD", "Playlists")

    val downloadedVaultItems = remember {
        listOf(
            DownloadedVaultItem("dv_1", "Monolith Sessions", "Solaris & Kaelen • 12 Tracks", "FLAC 96kHz • 642 MB", artSeed = 2),
            DownloadedVaultItem("dv_2", "Late Night Resonance", "Curated Vault • 24 Tracks", "BIT-PERFECT • 1.1 GB", artSeed = 1, iconType = "playlist"),
            DownloadedVaultItem("dv_3", "Subtle Echoes", "Mirage Architecture", "FLAC 192k/24b • 48.2 MB", artSeed = 3),
            DownloadedVaultItem("dv_4", "Velvet Cascade", "Aura & The Chamber Ensemble", "DSD 5.6MHz (128) • 124.0 MB", artSeed = 4),
            DownloadedVaultItem("dv_5", "Luminescence in D Minor", "Mira Thorne", "WAV 32b Float • 76.5 MB", artSeed = 5, iconType = "waves")
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_downloads_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp)
        ) {
            // 1. Top Bar (< Back | Downloads Vault | Tune, Avatar >)
            item(key = "vault_top_bar") {
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
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onBackClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Back",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Downloads Vault",
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

            // 2. Headline ("Offline Vault" + NVMe badge | Refresh & Storage buttons)
            item(key = "vault_headline") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Offline Vault",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 26.sp
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "NVMe",
                                style = ReonTokens.LabelMono,
                                color = onSurfaceMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Refresh Button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShowToast("Vault synced") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Sync",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Archive/Storage Button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .clickable { onShowToast("Storage Bay info") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Inbox,
                                contentDescription = "Storage",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 3. "BIT-PERFECT STORAGE BAY" Bento Card
            item(key = "vault_storage_bay_card") {
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
                        // Header: ● BIT-PERFECT STORAGE BAY + Manage button
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
                                    text = "BIT-PERFECT STORAGE BAY",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = onSurfaceMuted,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(surfaceHigh)
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp))
                                    .clickable { onShowToast("Storage settings") }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Manage",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = onSurface,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Large 1.42 GB of 128 GB Free
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "1.42 GB",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 28.sp
                            )
                            Text(
                                text = "of 128 GB Free",
                                style = MaterialTheme.typography.bodyMedium,
                                color = onSurfaceMuted,
                                fontSize = 13.5.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Segmented Progress Bar (Black, Dark Gray, Light Track)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(surfaceHigh)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(0.18f)
                                        .fillMaxHeight()
                                        .background(primaryColor)
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(0.12f)
                                        .fillMaxHeight()
                                        .background(Color(0xFF64748B))
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(0.70f)
                                        .fillMaxHeight()
                                        .background(Color.Transparent)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Legend Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(primaryColor)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = "DSD/FLAC (1.1 GB)",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.sp,
                                        color = onSurfaceMuted
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF64748B))
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = "Cache (320 MB)",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.sp,
                                        color = onSurfaceMuted
                                    )
                                }
                            }

                            Text(
                                text = "114.2 GB AVAIL",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        // Badges Row (Lossless Verified & Smart Sync On)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Lossless Verified Pill
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh)
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                    .padding(vertical = 8.dp),
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
                                        text = "Lossless Verified",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = onSurface,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }

                            // Smart Sync On Pill
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh)
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.Sync,
                                        contentDescription = null,
                                        tint = onSurface,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "Smart Sync On",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = onSurface,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. "In Transit" (Active Downloads / Transfers) Section
            item(key = "in_transit_section") {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "In Transit",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 17.sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(primaryColor)
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "2",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onPrimaryColor
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onShowToast("Paused downloads") }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Pause,
                                contentDescription = "Pause All",
                                tint = onSurfaceMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Pause All",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = onSurfaceMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // In Transit Card 1: Midnight City Lights 72%
                    InTransitDownloadCard(
                        title = "Midnight City Lights",
                        subtitle = "Solaris & Kaelen • Monolith Sessions",
                        progressPercent = 72,
                        metaLeft = "24-BIT / 96kHz FLAC • 63.4/88.0 MB",
                        metaRight = "6.8 MB/s • 3s",
                        iconType = "disc",
                        onPause = { onShowToast("Paused Midnight City Lights") },
                        onCancel = { onShowToast("Cancelled") }
                    )

                    Spacer(Modifier.height(8.dp))

                    // In Transit Card 2: Neon Horizons 45%
                    InTransitDownloadCard(
                        title = "Neon Horizons",
                        subtitle = "Aura Sound • Kinetic Resonance",
                        progressPercent = 45,
                        metaLeft = "DSD 11.2M (DSD256) • 123.3/274.0 MB",
                        metaRight = "4.2 MB/s • 18s",
                        iconType = "note",
                        onPause = { onShowToast("Paused Neon Horizons") },
                        onCancel = { onShowToast("Cancelled") }
                    )
                }
            }

            // 5. "Downloaded Vault" Section
            item(key = "downloaded_vault_header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Downloaded Vault",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 18.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onShowToast("Sort order") }
                        ) {
                            Text(
                                text = "SORT: RECENT",
                                style = ReonTokens.LabelMono,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = onSurfaceMuted
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                tint = onSurfaceMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // Vault Filter Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        vaultFilters.forEach { filter ->
                            val isSelected = filter == selectedFilter
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) primaryColor else surfaceHigh)
                                    .border(
                                        ReonSize.hairline,
                                        if (isSelected) primaryColor else hairlineColor,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filter,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) onPrimaryColor else onSurface,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Downloaded Items
            items(downloadedVaultItems, key = { it.id }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                        .clickable { onShowToast("Playing ${item.title}") }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Artwork with verified check overlay
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                    ) {
                        TrackArtImage(
                            url = getArtUrlForSeed(item.artSeed),
                            contentDescription = item.title,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Checked Badge Overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xE6000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Verified",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    // Title & Subtitles
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.5.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = item.formatMeta,
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = onSurfaceMuted
                        )
                    }

                    // Play Button Trigger
                    IconButton(
                        onClick = { onShowToast("Play ${item.title}") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play",
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Options (⋮)
                    IconButton(
                        onClick = { onShowToast("Options: ${item.title}") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More",
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 6. "VAULT SETTINGS" Section
            item(key = "vault_settings_section") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = null,
                                tint = onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "VAULT SETTINGS",
                                style = ReonTokens.LabelMono,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Text(
                            text = "PRESET #01",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Settings Bento Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(cardBackground)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            // Row 1: Master Download Quality
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Master Download Quality",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = onSurface,
                                        fontSize = 14.sp
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "DIRECT MASTER (FLAC 24/96)",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.5.sp,
                                        color = onSurfaceMuted
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(surfaceHigh)
                                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp))
                                        .clickable { onShowToast("Change Quality") }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Change",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = onSurface,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            HorizontalDivider(
                                thickness = ReonSize.hairline,
                                color = hairlineColor,
                                modifier = Modifier.padding(vertical = 14.dp)
                            )

                            // Row 2: Download via Wi-Fi only
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Download via Wi-Fi only",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = onSurface,
                                        fontSize = 14.sp
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "Conserve cellular bandwidth",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = onSurfaceMuted,
                                        fontSize = 12.sp
                                    )
                                }

                                Switch(
                                    checked = wifiOnlyEnabled,
                                    onCheckedChange = { wifiOnlyEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = onPrimaryColor,
                                        checkedTrackColor = primaryColor,
                                        uncheckedThumbColor = onSurfaceMuted,
                                        uncheckedTrackColor = surfaceHigh
                                    )
                                )
                            }

                            Spacer(Modifier.height(14.dp))

                            // Row 3: Audio Formats & Codec Priority Button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh)
                                    .clickable { onShowToast("Codec Priority") },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Audio Formats & Codec Priority",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = onSurface,
                                        fontSize = 13.5.sp
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                        contentDescription = null,
                                        tint = onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InTransitDownloadCard(
    title: String,
    subtitle: String,
    progressPercent: Int,
    metaLeft: String,
    metaRight: String,
    iconType: String,
    onPause: () -> Unit,
    onCancel: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 42dp Thumbnail Glyph
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconType == "disc") {
                        Icon(
                            imageVector = Icons.Rounded.GraphicEq,
                            contentDescription = null,
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // Title, percent, and action icons
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 14.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        Text(
                            text = "$progressPercent%",
                            style = ReonTokens.LabelMono,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceMuted,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = onPause, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Rounded.Pause, contentDescription = "Pause", tint = onSurface, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = onCancel, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Cancel", tint = onSurfaceMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(surfaceHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressPercent / 100f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(primaryColor)
                )
            }

            Spacer(Modifier.height(6.dp))

            // Meta Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = metaLeft,
                    style = ReonTokens.LabelMono,
                    fontSize = 10.sp,
                    color = onSurfaceMuted
                )
                Text(
                    text = metaRight,
                    style = ReonTokens.LabelMono,
                    fontSize = 10.sp,
                    color = onSurfaceMuted
                )
            }
        }
    }
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
