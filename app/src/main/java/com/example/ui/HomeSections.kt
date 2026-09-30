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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Adjust
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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

// ==========================================
// 1. Top Bar (REON [STUDIO] | Notifications, Tune, Avatar)
// ==========================================
@Composable
fun HomeTopBar(
    onAnalyticsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onFiltersClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: REON + STUDIO badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "REON",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = onSurface,
                fontSize = 21.sp,
                letterSpacing = 1.4.sp
            )

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(surfaceHigh)
                    .border(0.8.dp, hairlineColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "STUDIO",
                    style = ReonTokens.LabelMono,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.reonExtras.onSurfaceMuted,
                    letterSpacing = 0.8.sp
                )
            }
        }

        // Right: Notification Bell, Filter Tune, Avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }

            IconButton(
                onClick = onFiltersClick,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Tune,
                    contentDescription = "Filters",
                    tint = onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(2.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(onSurface)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==========================================
// 2. Greeting Header Block (TUESDAY, OCTOBER 24 | ● LOSSLESS 24-BIT | Good evening)
// ==========================================
@Composable
fun HomeGreetingHeader(
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TUESDAY, OCTOBER 24",
                style = ReonTokens.LabelMono,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = onSurfaceMuted,
                letterSpacing = 0.8.sp
            )

            // Right Pill: ● LOSSLESS 24-BIT
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceHigh)
                    .border(0.8.dp, hairlineColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(onSurface)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "LOSSLESS 24-BIT",
                        style = ReonTokens.LabelMono,
                        fontSize = 9.5.sp,
                        color = onSurface,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Good evening",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = onSurface,
            fontSize = 32.sp,
            letterSpacing = (-0.5).sp
        )
    }
}

// ==========================================
// 3. Category Filter Chips (All, Playlists, Albums, Artists, Hi-Res)
// ==========================================
@Composable
fun HomeCategoryChips(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Playlists", "Albums", "Artists", "Hi-Res")
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .height(34.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(if (isSelected) onSurface else surfaceHigh)
                    .border(
                        0.8.dp,
                        if (isSelected) onSurface else hairlineColor,
                        RoundedCornerShape(17.dp)
                    )
                    .clickable { onCategorySelect(category) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.surface else onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.5.sp
                )
            }
        }
    }
}

// ==========================================
// 4. Featured Release Hero Card (Nocturne Sessions Vol. IV)
// ==========================================
@Composable
fun HomeHeroAcousticResidencyCard(
    onStreamMasterClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(20.dp))
    ) {
        Column {
            // Hero Image Container with Badges & Title Overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                TrackArtImage(
                    url = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=800&q=80",
                    contentDescription = "Nocturne Sessions Vol. IV",
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for pristine contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // Top-Left Badge: [◎] FEATURED RELEASE
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Adjust,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "FEATURED RELEASE",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                    }
                }

                // Bottom Content inside image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "NEW RECORDING",
                        style = ReonTokens.LabelMono,
                        fontSize = 10.5.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Nocturne Sessions Vol. IV",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Solaris & Kaelen • 24-Bit / 192kHz Lossless",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.5.sp
                    )
                }
            }

            // Bottom Actions Bar: [Play Album] [+] [...] DURATION 9 Tracks • 48 min
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Solid Black "Play Album" button
                    Box(
                        modifier = Modifier
                            .height(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(onSurface)
                            .clickable(onClick = onStreamMasterClick)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Play Album",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = 13.5.sp
                            )
                        }
                    }

                    // Plus Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                            .clickable(onClick = onAddClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add",
                            tint = onSurface,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // More Options Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                            .clickable(onClick = onMoreClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreHoriz,
                            contentDescription = "More",
                            tint = onSurface,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Right Duration metadata
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DURATION",
                        style = ReonTokens.LabelMono,
                        fontSize = 9.sp,
                        color = onSurfaceMuted,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = "9 Tracks • 48 min",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = onSurface
                    )
                }
            }
        }
    }
}

// ==========================================
// 5. Jump Back In (2x2 Grid with Progress)
// ==========================================
@Composable
fun HomeJumpBackInGrid(
    onTrackSelect: (TrackItem) -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted

    val items = listOf(
        Triple("Neon Ho...", "Echo Drift", 0.65f),
        Triple("Subtle E...", "Kaelen Solo", 0.35f),
        Triple("Obsidian ...", "Mirage En...", 0.80f),
        Triple("Equinox ...", "Aura Soun...", 0.45f)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        // Section Header: Jump Back In | SEE ALL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jump Back In",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = onSurface,
                fontSize = 19.sp
            )

            Text(
                text = "SEE ALL",
                style = ReonTokens.LabelMono,
                fontSize = 11.sp,
                color = onSurfaceMuted,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(Modifier.height(10.dp))

        // 2x2 Bento Grid
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                JumpBackInCard(
                    title = items[0].first,
                    subtitle = items[0].second,
                    progress = items[0].third,
                    imageUrl = "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=300&q=80",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTrackSelect(
                            TrackItem("jb_1", "Neon Horizons", "Echo Drift", "Resume", "4:18", badge = "24-BIT", artSeed = 1)
                        )
                    }
                )

                JumpBackInCard(
                    title = items[1].first,
                    subtitle = items[1].second,
                    progress = items[1].third,
                    imageUrl = "https://images.unsplash.com/photo-1520523839898-507128fc543a?w=300&q=80",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTrackSelect(
                            TrackItem("jb_2", "Subtle Echoes", "Kaelen Solo", "Resume", "5:14", badge = "FLAC", artSeed = 2)
                        )
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                JumpBackInCard(
                    title = items[2].first,
                    subtitle = items[2].second,
                    progress = items[2].third,
                    imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=300&q=80",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTrackSelect(
                            TrackItem("jb_3", "Obsidian Monolith", "Mirage Ensemble", "Resume", "3:40", badge = "HI-RES", artSeed = 3)
                        )
                    }
                )

                JumpBackInCard(
                    title = items[3].first,
                    subtitle = items[3].second,
                    progress = items[3].third,
                    imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&q=80",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTrackSelect(
                            TrackItem("jb_4", "Equinox Waves", "Aura Soundworks", "Resume", "4:50", badge = "DSD", artSeed = 4)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun JumpBackInCard(
    title: String,
    subtitle: String,
    progress: Float,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceHigh)
                ) {
                    TrackArtImage(
                        url = imageUrl,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.5.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        color = onSurfaceMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Playback progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.5.dp)
                    .clip(CircleShape)
                    .background(surfaceHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(onSurface)
                )
            }
        }
    }
}

// ==========================================
// 6. Curated Playlists (Horizontal Carousel)
// ==========================================
@Composable
fun HomeCuratedVaultsRow(
    onVaultSelect: (String) -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        // Section Header: Curated Playlists | SEE ALL (18)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Curated Playlists",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = onSurface,
                fontSize = 19.sp
            )

            Text(
                text = "SEE ALL (18)",
                style = ReonTokens.LabelMono,
                fontSize = 11.sp,
                color = onSurfaceMuted,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Late Night Resonance (AMBIENT)
            CuratedPlaylistCard(
                title = "Late Night Resonance",
                subtitle = "Deep spatial soundscapes",
                metadata = "28 TRACKS • 2H 42M",
                tag = "AMBIENT",
                imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=600&q=80",
                onClick = { onVaultSelect("Late Night Resonance") }
            )

            // Card 2: Architectural Lows (MODULAR)
            CuratedPlaylistCard(
                title = "Architectural Lows",
                subtitle = "Sub-bass & precision mod",
                metadata = "19 TRACKS • 1H 54M",
                tag = "MODULAR",
                imageUrl = "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=600&q=80",
                onClick = { onVaultSelect("Architectural Lows") }
            )

            // Card 3: Crystalline Frequencies (MINIMAL)
            CuratedPlaylistCard(
                title = "Crystalline Space",
                subtitle = "Pure acoustic harmonics",
                metadata = "24 TRACKS • 2H 15M",
                tag = "MINIMAL",
                imageUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600&q=80",
                onClick = { onVaultSelect("Crystalline Space") }
            )
        }
    }
}

@Composable
private fun CuratedPlaylistCard(
    title: String,
    subtitle: String,
    metadata: String,
    tag: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest

    Column(
        modifier = Modifier
            .width(210.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(210.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cardBackground)
                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
        ) {
            TrackArtImage(
                url = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize()
            )

            // Tag Pill bottom-left: AMBIENT, MODULAR
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tag,
                    style = ReonTokens.LabelMono,
                    fontSize = 9.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = onSurface,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(1.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = onSurfaceMuted,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = metadata,
            style = ReonTokens.LabelMono,
            fontSize = 9.5.sp,
            color = onSurfaceMuted,
            letterSpacing = 0.4.sp
        )
    }
}

// ==========================================
// 7. Quick Picks (Vertical Track List with Play Trigger)
// ==========================================
@Composable
fun HomeHeavyRotationList(
    onTrackSelect: (TrackItem) -> Unit = {},
    onPlayAllClick: () -> Unit = {},
    onShowToast: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest

    val tracks = listOf(
        TrackItem("qp_1", "Crystalline Dispersion", "Solaris & Aura Lab", "Quick", "6:42", badge = "LOSSLESS", artSeed = 1),
        TrackItem("qp_2", "Nordic Tectonic Drift", "Kaelen Acoustic Works", "Quick", "8:19", badge = "HI-RES", artSeed = 2),
        TrackItem("qp_3", "Monolith Variations: Pt. II", "Mirage Architecture", "Quick", "5:12", badge = "LOSSLESS", artSeed = 3),
        TrackItem("qp_4", "Glass & Anodized Steel", "Reon Sound Lab Ensemble", "Quick", "4:58", badge = "HI-RES", artSeed = 4)
    )

    val imageUrls = listOf(
        "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=300&q=80",
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=300&q=80",
        "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=300&q=80",
        "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&q=80"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        // Header: Quick Picks | PLAY ALL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Picks",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = onSurface,
                fontSize = 19.sp
            )

            Text(
                text = "PLAY ALL",
                style = ReonTokens.LabelMono,
                fontSize = 11.sp,
                color = onSurfaceMuted,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                modifier = Modifier.clickable { onPlayAllClick() }
            )
        }

        Spacer(Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            tracks.forEachIndexed { index, track ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                        .clickable { onTrackSelect(track) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Album Art Thumbnail
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                    ) {
                        TrackArtImage(
                            url = imageUrls[index % imageUrls.size],
                            contentDescription = track.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Title + Badge & Artist
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
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            if (track.badge.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(surfaceHigh)
                                        .border(0.5.dp, hairlineColor, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
                                ) {
                                    Text(
                                        text = track.badge,
                                        style = ReonTokens.LabelMono,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
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
                            fontSize = 11.5.sp,
                            maxLines = 1
                        )
                    }

                    // Duration
                    Text(
                        text = track.duration,
                        style = ReonTokens.LabelMono,
                        fontSize = 11.5.sp,
                        color = onSurfaceMuted
                    )

                    Spacer(Modifier.width(8.dp))

                    // Circular Play Button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, CircleShape)
                            .clickable { onTrackSelect(track) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play",
                            tint = onSurface,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(Modifier.width(4.dp))

                    // More Options Icon
                    IconButton(
                        onClick = { onShowToast("Options for ${track.title}") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More",
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. Artists You May Like (Horizontal Row with Follow Buttons)
// ==========================================
@Composable
fun HomeArtistsInResidenceRow(
    onArtistSelect: (String) -> Unit = {},
    onFollowClick: (String) -> Unit = {},
    onExploreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        // Section Header: Artists You May Like | EXPLORE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Artists You May Like",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = onSurface,
                fontSize = 19.sp
            )

            Text(
                text = "EXPLORE",
                style = ReonTokens.LabelMono,
                fontSize = 11.sp,
                color = onSurfaceMuted,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                modifier = Modifier.clickable { onExploreClick() }
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Artist 1: Solaris & Kaelen
            ArtistFollowCard(
                name = "Solaris & Kaelen",
                location = "Oslo, Norway",
                releases = "14 Releases",
                imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
                showRadio = true,
                onArtistClick = { onArtistSelect("Solaris & Kaelen") },
                onFollowClick = { onFollowClick("Solaris & Kaelen") }
            )

            // Artist 2: Aura Sound Lab
            ArtistFollowCard(
                name = "Aura Sound Lab",
                location = "Stockholm, Sweden",
                releases = "9 Releases",
                imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80",
                showRadio = false,
                onArtistClick = { onArtistSelect("Aura Sound Lab") },
                onFollowClick = { onFollowClick("Aura Sound Lab") }
            )

            // Artist 3: Mirage Ensemble
            ArtistFollowCard(
                name = "Mirage Ensemble",
                location = "Berlin, Germany",
                releases = "11 Releases",
                imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&q=80",
                showRadio = false,
                onArtistClick = { onArtistSelect("Mirage Ensemble") },
                onFollowClick = { onFollowClick("Mirage Ensemble") }
            )
        }
    }
}

@Composable
private fun ArtistFollowCard(
    name: String,
    location: String,
    releases: String,
    imageUrl: String,
    showRadio: Boolean = false,
    onArtistClick: () -> Unit,
    onFollowClick: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh

    Box(
        modifier = Modifier
            .width(235.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onArtistClick)
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceHigh)
                ) {
                    TrackArtImage(
                        url = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        fontSize = 14.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        color = onSurfaceMuted
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = releases,
                        style = ReonTokens.LabelMono,
                        fontSize = 9.sp,
                        color = onSurfaceMuted
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Solid Black Follow Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(onSurface)
                        .clickable(onClick = onFollowClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Follow",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 12.5.sp
                    )
                }

                if (showRadio) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Radio,
                            contentDescription = "Radio",
                            tint = onSurface,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}
