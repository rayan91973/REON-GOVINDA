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
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.R
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * REON — Main Library Screen
 * Provides comprehensive hub for:
 * 1. Liked Songs (Bit-Perfect Repository)
 * 2. Listening History (Session Timeline)
 * 3. Downloads (Offline Lossless Vault)
 * 4. Curated Playlists & Vaults
 * 5. Saved Albums & Discographies
 */
@Composable
fun LibraryScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit,
    onOpenLikedSongs: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenDownloads: () -> Unit,
    onOpenPlaylist: (PlaylistItem) -> Unit,
    onOpenAlbum: (AlbumItem) -> Unit,
    onOpenArtist: (ArtistItem?) -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenAnalytics: () -> Unit,
    onOpenSettings: () -> Unit,
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

    var selectedFilter by remember { mutableStateOf("All") }
    val filterTabs = listOf("All", "Playlists", "Albums", "Artists", "Downloaded")

    val playlists = remember {
        listOf(
            PlaylistItem("pl_1", "Deep Focus // Electric Light", "Subtle ambient textures & modular synthesis", "28 tracks", "1h 30m", 1),
            PlaylistItem("pl_2", "Late Night Resonance", "Ambient & Deep Spatial Works", "28 tracks", "2h 42m", 2),
            PlaylistItem("pl_3", "Architectural Lows", "Deep Sub-Bass & Modular Synth Arrays", "19 tracks", "1h 54m", 3),
            PlaylistItem("pl_4", "Kinetic Space", "High-frequency precision transients", "15 tracks", "1h 12m", 4)
        )
    }

    val albums = remember {
        listOf(
            AlbumItem("alb_1", "Monolith Sessions", "Solaris & Kaelen", "2024", "12 tracks", "Electronic", 1),
            AlbumItem("alb_2", "Resonance EP", "Aura Sound Lab", "2023", "5 tracks", "Ambient", 2),
            AlbumItem("alb_3", "Refractions", "Solaris & Kaelen", "2022", "9 tracks", "Modular", 3)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_library_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 180.dp)
        ) {
            // 1. Top Bar (< ■ Library | Analytics, Notifications, Tune, Profile >)
            item(key = "lib_top_bar") {
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
                            fontSize = 19.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Analytics
                        IconButton(
                            onClick = onOpenAnalytics,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.BarChart,
                                contentDescription = "Analytics",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Notifications
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = "Notifications",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Tune
                        IconButton(
                            onClick = { onShowToast("Library filters") },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = "Tune",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Profile
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(onSurface)
                                .clickable { onOpenSettings() },
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

            // 2. Primary Vault Hub (Liked Songs, History, Downloads Bento Cards)
            item(key = "lib_primary_vault_hub") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                ) {
                    // Card 1: Liked Songs (Large Hero Bento Card)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF141416))
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                            .clickable { onOpenLikedSongs() }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Favorite,
                                        contentDescription = "Liked Songs",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(Modifier.width(14.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Liked Songs",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 17.sp
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color.White.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "VAULT 01",
                                                style = ReonTokens.LabelMono,
                                                fontSize = 8.5.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "148 Master Tracks • 9h 42m • FLAC/DSD 96kHz",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.65f)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Open",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // 2-Column Sub-Hub: Listening History & Downloads
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Tile 1: Listening History
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBackground)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                                .clickable { onOpenHistory() }
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(surfaceHigh),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.History,
                                            contentDescription = null,
                                            tint = onSurface,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Text(
                                        text = "3h 14m",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = onSurface
                                    )
                                }

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    text = "Listening History",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    fontSize = 14.sp
                                )
                                Spacer(Modifier.height(1.dp))
                                Text(
                                    text = "LOG.REON.SESSION_092",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 9.sp,
                                    color = onSurfaceMuted
                                )
                            }
                        }

                        // Tile 2: Downloads (Offline Vault)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBackground)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                                .clickable { onOpenDownloads() }
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(surfaceHigh),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Download,
                                            contentDescription = null,
                                            tint = onSurface,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Text(
                                        text = "4.2 GB",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = onSurface
                                    )
                                }

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    text = "Downloads Vault",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    fontSize = 14.sp
                                )
                                Spacer(Modifier.height(1.dp))
                                Text(
                                    text = "14 BIT-PERFECT TRACKS",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 9.sp,
                                    color = onSurfaceMuted
                                )
                            }
                        }
                    }
                }
            }

            // 3. Filter Chips
            item(key = "lib_filter_chips") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterTabs.forEach { tab ->
                        val isSelected = tab == selectedFilter
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
                                .clickable {
                                    selectedFilter = tab
                                    if (tab == "Downloaded") onOpenDownloads()
                                }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) onPrimaryColor else onSurface,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // 4. Playlists Section
            item(key = "lib_playlists_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = ReonSpacing.margin, end = ReonSpacing.margin, top = 12.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PLAYLISTS & CURATED VAULTS",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "${playlists.size} VAULTS",
                        style = ReonTokens.LabelMono,
                        fontSize = 10.5.sp,
                        color = onSurfaceMuted
                    )
                }
            }

            items(playlists, key = { it.id }) { playlist ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 3.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                        .clickable { onOpenPlaylist(playlist) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                    ) {
                        TrackArtImage(
                            url = getArtUrlForSeed(playlist.artSeed),
                            contentDescription = playlist.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = playlist.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 14.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${playlist.trackCount} • ${playlist.duration}",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = onSurfaceMuted
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = onSurfaceMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 5. Albums Section
            item(key = "lib_albums_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = ReonSpacing.margin, end = ReonSpacing.margin, top = 16.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SAVED ALBUMS",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "${albums.size} RELEASES",
                        style = ReonTokens.LabelMono,
                        fontSize = 10.5.sp,
                        color = onSurfaceMuted
                    )
                }
            }

            items(albums, key = { it.id }) { album ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 3.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                        .clickable { onOpenAlbum(album) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                    ) {
                        TrackArtImage(
                            url = getArtUrlForSeed(album.artSeed + 2),
                            contentDescription = album.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = album.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 14.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${album.artist} • ${album.trackCount} • ${album.year}",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = onSurfaceMuted
                        )
                    }
                }
            }

            // 6. NVMe Local Storage Footer
            item(key = "lib_storage_footer") {
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
                            text = "NVME LOCAL STORAGE: 4.2 GB / 128 GB",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Direct memory mapping enabled • Zero compression artifacting",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceMuted,
                        fontSize = 11.sp
                    )
                }
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
