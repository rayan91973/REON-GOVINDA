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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Radio
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
// 1. Top Bar (< ■ REON | Analytics, Notifications, Tune, Avatar >)
// ==========================================
@Composable
fun HomeTopBar(
    onAnalyticsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onFiltersClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val onSurface = MaterialTheme.colorScheme.onSurface

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
                text = "REON",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = onSurface,
                fontSize = 18.sp,
                letterSpacing = 1.2.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onAnalyticsClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.BarChart,
                    contentDescription = "Analytics",
                    tint = onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Notifications",
                    tint = onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onFiltersClick,
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
                    .clickable { onProfileClick() },
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

// ==========================================
// 2. Greeting Header Block (FEED // 010.4 | Good evening, Listener | ● 96KHZ • 24-BIT)
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
            .padding(horizontal = ReonSpacing.margin, vertical = 4.dp)
    ) {
        Text(
            text = "FEED // 010.4",
            style = ReonTokens.LabelMono,
            fontSize = 10.5.sp,
            color = onSurfaceMuted,
            letterSpacing = 0.5.sp
        )

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Good evening, Listener",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = onSurface,
                fontSize = 24.sp
            )

            // Right Pill: ● 96KHZ • 24-BIT
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceHigh)
                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
                        text = "96KHZ • 24-BIT",
                        style = ReonTokens.LabelMono,
                        fontSize = 9.5.sp,
                        color = onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. Category Filter Chips (All Focus, Hi-Res Masters, Curated Vaults, Ambient)
// ==========================================
@Composable
fun HomeCategoryChips(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All Focus", "Hi-Res Masters", "Curated Vaults", "Ambient")
    val onSurface = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
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
                    .clickable { onCategorySelect(category) }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) onPrimaryColor else onSurface,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ==========================================
// 4. Hero Featured Acoustic Residency Card
// ==========================================
@Composable
fun HomeHeroAcousticResidencyCard(
    onStreamMasterClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReonSpacing.margin, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column {
            // Hero Image with Overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.art_refractions),
                    contentDescription = "Nocturne Trance Sessions Vol. IV",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Top-Left Badge: ✦ MASTER CUT • 24-BIT / 192KHZ FLAC
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xD9000000))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "✦ MASTER CUT • 24-BIT / 192KHZ FLAC",
                            style = ReonTokens.LabelMono,
                            fontSize = 9.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Bottom Content inside image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "ACOUSTIC RESIDENCY № 04",
                        style = ReonTokens.LabelMono,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Nocturne Trance Sessions Vol. IV",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 17.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Solaris & Kaelen feat. Aura • Studio Soundstage...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 11.5.sp
                    )
                }
            }

            // Bottom Transport Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Solid Black Stream Master Button
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryColor)
                            .clickable(onClick = onStreamMasterClick)
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint = onPrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Stream Master",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = onPrimaryColor,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Bookmark Button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                            .clickable(onClick = onBookmarkClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Right: DYNAMIC RANGE / DR14 • BIT-DIRECT
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DYNAMIC RANGE",
                        style = ReonTokens.LabelMono,
                        fontSize = 9.sp,
                        color = onSurfaceMuted,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = "DR14 • BIT-DIRECT",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurface
                    )
                }
            }
        }
    }
}

// ==========================================
// 5. "■ Jump Back In" (Resume Dock)
// ==========================================
@Composable
fun HomeJumpBackInGrid(
    onTrackSelect: (TrackItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    val items = listOf(
        Triple("Neon Horizons", "ECHO DRIFT • DSD 256", 0.35f),
        Triple("Subtle Echoes", "KAELEN SOLO • FLAC 96/24", 0.55f),
        Triple("Obsidian Echoes", "MIRAGE ENSEMBLE • PCM 192k", 0.75f),
        Triple("Equinox Redux", "AURA SOUND LAB • DSD 5.6M", 0.45f)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(onSurface, RoundedCornerShape(1.dp))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Jump Back In",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "RESUME DOCK",
                style = ReonTokens.LabelMono,
                fontSize = 10.5.sp,
                color = onSurfaceMuted
            )
        }

        Spacer(Modifier.height(8.dp))

        // 2x2 Bento Grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Item 1
                JumpBackInCard(
                    title = items[0].first,
                    meta = items[0].second,
                    progress = items[0].third,
                    seed = 1,
                    modifier = Modifier.weight(1f),
                    onClick = { onTrackSelect(TrackItem("jb_1", items[0].first, "Echo Drift", "Resume", "4:18", artSeed = 1)) }
                )
                // Item 2
                JumpBackInCard(
                    title = items[1].first,
                    meta = items[1].second,
                    progress = items[1].third,
                    seed = 2,
                    modifier = Modifier.weight(1f),
                    onClick = { onTrackSelect(TrackItem("jb_2", items[1].first, "Kaelen Solo", "Resume", "5:14", artSeed = 2)) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Item 3
                JumpBackInCard(
                    title = items[2].first,
                    meta = items[2].second,
                    progress = items[2].third,
                    seed = 3,
                    modifier = Modifier.weight(1f),
                    onClick = { onTrackSelect(TrackItem("jb_3", items[2].first, "Mirage Ensemble", "Resume", "3:40", artSeed = 3)) }
                )
                // Item 4
                JumpBackInCard(
                    title = items[3].first,
                    meta = items[3].second,
                    progress = items[3].third,
                    seed = 4,
                    modifier = Modifier.weight(1f),
                    onClick = { onTrackSelect(TrackItem("jb_4", items[3].first, "Aura Sound Lab", "Resume", "4:50", artSeed = 4)) }
                )
            }
        }
    }
}

@Composable
private fun JumpBackInCard(
    title: String,
    meta: String,
    progress: Float,
    seed: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(surfaceHigh)
                ) {
                    TrackArtImage(
                        url = getArtUrlForSeed(seed),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = meta,
                        style = ReonTokens.LabelMono,
                        fontSize = 9.sp,
                        color = onSurfaceMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(surfaceHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(primaryColor)
                )
            }
        }
    }
}

// ==========================================
// 6. "■ Curated Vaults"
// ==========================================
@Composable
fun HomeCuratedVaultsRow(
    onVaultSelect: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ReonSpacing.margin),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(onSurface, RoundedCornerShape(1.dp))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Curated Vaults",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "VIEW ALL (18)",
                style = ReonTokens.LabelMono,
                fontSize = 10.5.sp,
                color = onSurfaceMuted,
                modifier = Modifier.clickable { onVaultSelect("all") }
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = ReonSpacing.margin),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Vault 1: Late Night Resonance
            Column(
                modifier = Modifier
                    .width(180.dp)
                    .clickable { onVaultSelect("Late Night Resonance") }
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.art_refractions),
                        contentDescription = "Late Night Resonance",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Badge: DSD 5.6MHz
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xD9000000))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "DSD 5.6MHz",
                            style = ReonTokens.LabelMono,
                            fontSize = 8.5.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Late Night Resonance",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 14.sp
                )
                Text(
                    text = "Ambient & Deep Spatial Works",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceMuted,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "28 TRACKS  •  2H 42M",
                    style = ReonTokens.LabelMono,
                    fontSize = 9.5.sp,
                    color = onSurfaceMuted
                )
            }

            // Vault 2: Architectural Lows
            Column(
                modifier = Modifier
                    .width(180.dp)
                    .clickable { onVaultSelect("Architectural Lows") }
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.art_refractions),
                        contentDescription = "Architectural Lows",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Badge: 24-BIT / 192k
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xD9000000))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "24-BIT / 192k",
                            style = ReonTokens.LabelMono,
                            fontSize = 8.5.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Architectural Lows",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 14.sp
                )
                Text(
                    text = "Deep Sub-Bass & Modular...",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceMuted,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "19 TRACKS  •  1H 54M",
                    style = ReonTokens.LabelMono,
                    fontSize = 9.5.sp,
                    color = onSurfaceMuted
                )
            }
        }
    }
}

// ==========================================
// 7. "■ Heavy Rotation" (01 to 04 with play triggers)
// ==========================================
@Composable
fun HomeHeavyRotationList(
    onTrackSelect: (TrackItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    val tracks = listOf(
        TrackItem("hr_1", "Crystalline Dispersion", "Solaris & Aura Lab", "Heavy", "6:42", badge = "DSD", artSeed = 1),
        TrackItem("hr_2", "Nordic Tectonic Drift", "Kaelen Acoustic Works", "Heavy", "8:19", badge = "24-BIT", artSeed = 2),
        TrackItem("hr_3", "Monolith Variations: Pt. II", "Mirage Architecture", "Heavy", "5:12", badge = "HI-RES", artSeed = 3),
        TrackItem("hr_4", "Glass & Anodized Steel", "Reon Sound Lab Ensemble", "Heavy", "4:58", badge = "DSD", artSeed = 4)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(onSurface, RoundedCornerShape(1.dp))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Heavy Rotation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "GLOBAL METRICS",
                style = ReonTokens.LabelMono,
                fontSize = 10.5.sp,
                color = onSurfaceMuted
            )
        }

        Spacer(Modifier.height(8.dp))

        tracks.forEachIndexed { index, track ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTrackSelect(track) }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Track Number (01, 02...)
                Text(
                    text = String.format("%02d", index + 1),
                    style = ReonTokens.LabelMono,
                    fontSize = 12.sp,
                    color = onSurfaceMuted,
                    modifier = Modifier.width(28.dp)
                )

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
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(surfaceHigh)
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
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

                    Spacer(Modifier.height(1.dp))

                    Text(
                        text = track.artist,
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceMuted,
                        fontSize = 12.sp,
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

                Spacer(Modifier.width(10.dp))

                // Circular Play Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
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
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 8. "■ New Lossless Masters"
// ==========================================
@Composable
fun HomeNewLosslessMastersRow(
    onMasterSelect: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ReonSpacing.margin),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(onSurface, RoundedCornerShape(1.dp))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "New Lossless Masters",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "STUDIO DIRECT",
                style = ReonTokens.LabelMono,
                fontSize = 10.5.sp,
                color = onSurfaceMuted
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = ReonSpacing.margin),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Master 1: Monolith Sessions (2025)
            Column(
                modifier = Modifier
                    .width(170.dp)
                    .clickable { onMasterSelect("Monolith Sessions") }
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.art_refractions),
                        contentDescription = "Monolith Sessions",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xD9000000))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "2025",
                            style = ReonTokens.LabelMono,
                            fontSize = 9.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Monolith Sessions",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 14.sp
                )
                Text(
                    text = "Solaris & Kaelen",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceMuted,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    text = "8 TRACKS  •  192kHz",
                    style = ReonTokens.LabelMono,
                    fontSize = 9.5.sp,
                    color = onSurfaceMuted
                )
            }

            // Master 2: Resonance EP (2025)
            Column(
                modifier = Modifier
                    .width(170.dp)
                    .clickable { onMasterSelect("Resonance EP") }
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.art_refractions),
                        contentDescription = "Resonance EP",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xD9000000))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "2025",
                            style = ReonTokens.LabelMono,
                            fontSize = 9.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Resonance EP",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 14.sp
                )
                Text(
                    text = "Aura Sound Lab",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceMuted,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    text = "5 TRACKS  •  DSD 128",
                    style = ReonTokens.LabelMono,
                    fontSize = 9.5.sp,
                    color = onSurfaceMuted
                )
            }
        }
    }
}

// ==========================================
// 9. "■ Artists in Residence"
// ==========================================
@Composable
fun HomeArtistsInResidenceRow(
    onArtistSelect: (String) -> Unit = {},
    onFollowClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ReonSpacing.margin),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(onSurface, RoundedCornerShape(1.dp))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Artists in Residence",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "ARCHIVES",
                style = ReonTokens.LabelMono,
                fontSize = 10.5.sp,
                color = onSurfaceMuted
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = ReonSpacing.margin),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Artist 1: Solaris & Kaelen
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardBackground)
                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                    .clickable { onArtistSelect("Solaris & Kaelen") }
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                        ) {
                            TrackArtImage(
                                url = getArtUrlForSeed(1),
                                contentDescription = "Solaris & Kaelen",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Solaris & Kaelen",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(1.dp))
                            Text(
                                text = "OSLO, NORWAY",
                                style = ReonTokens.LabelMono,
                                fontSize = 9.sp,
                                color = onSurfaceMuted
                            )
                            Text(
                                text = "14 BIT-PERFECT MASTERS",
                                style = ReonTokens.LabelMono,
                                fontSize = 8.5.sp,
                                color = onSurfaceMuted
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(primaryColor)
                                .clickable { onFollowClick("Solaris & Kaelen") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Follow",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = onPrimaryColor,
                                fontSize = 11.5.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Radio,
                                contentDescription = "Radio",
                                tint = onSurface,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            // Artist 2: Aura Sound
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardBackground)
                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                    .clickable { onArtistSelect("Aura Sound") }
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                        ) {
                            TrackArtImage(
                                url = getArtUrlForSeed(2),
                                contentDescription = "Aura Sound",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Aura Sound",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(1.dp))
                            Text(
                                text = "STOCKHOLM, SWEDEN",
                                style = ReonTokens.LabelMono,
                                fontSize = 9.sp,
                                color = onSurfaceMuted
                            )
                            Text(
                                text = "9 DSD MASTERS",
                                style = ReonTokens.LabelMono,
                                fontSize = 8.5.sp,
                                color = onSurfaceMuted
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(primaryColor)
                            .clickable { onFollowClick("Aura Sound") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Follow",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = onPrimaryColor,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 10. "☵ REON Acoustic Architecture" Telemetry Card
// ==========================================
@Composable
fun HomeTelemetryArchitectureCard(
    modifier: Modifier = Modifier
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReonSpacing.margin, vertical = 10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
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
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "REON Acoustic Architecture",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        fontSize = 13.5.sp
                    )
                }

                Text(
                    text = "CORE 3.4.1",
                    style = ReonTokens.LabelMono,
                    fontSize = 10.sp,
                    color = onSurfaceMuted
                )
            }

            Spacer(Modifier.height(10.dp))

            // 3-Column Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tile 1: SIGNAL PATH
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceHigh)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SIGNAL PATH", style = ReonTokens.LabelMono, fontSize = 8.5.sp, color = onSurfaceMuted)
                        Spacer(Modifier.height(2.dp))
                        Text("DIRECT DSD", style = ReonTokens.LabelMono, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = onSurface)
                    }
                }

                // Tile 2: LATENCY
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceHigh)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LATENCY", style = ReonTokens.LabelMono, fontSize = 8.5.sp, color = onSurfaceMuted)
                        Spacer(Modifier.height(2.dp))
                        Text("0.8 MS", style = ReonTokens.LabelMono, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = onSurface)
                    }
                }

                // Tile 3: DAC SYNC
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceHigh)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("DAC SYNC", style = ReonTokens.LabelMono, fontSize = 8.5.sp, color = onSurfaceMuted)
                        Spacer(Modifier.height(2.dp))
                        Text("LOCKED", style = ReonTokens.LabelMono, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = onSurface)
                    }
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
