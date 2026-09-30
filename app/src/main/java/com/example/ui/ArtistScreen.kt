package com.example.ui

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
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Verified
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * REON — Artist Profile Screen (Theme Unified)
 * Fully cohesive with REON's minimalist dark Brutalist / Sona audio engine aesthetic:
 * - Dynamic Material 3 color system (OLED canvas, surface containers, crisp typography)
 * - Hero header card with studio background, format badge & verified check
 * - High-contrast Following / Radio / Play control dock
 * - REON Master Acoustic Profile bento card
 * - Bit-perfect Popular Tracks list with live eq indicators
 * - Discography & Master Vault horizontal row
 * - Engineering Editorial Note & Sound Lab specs card
 */
@Composable
fun ArtistScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit,
    onAlbumSelect: (AlbumItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFollowToggle: () -> Unit = {},
    onRadioClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
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

    val artist = state.selectedArtist ?: ArtistItem(
        id = "art_aurora",
        name = state.activeArtistName,
        genre = "Electronic",
        isFollowing = state.isArtistFollowed,
        artSeed = 1
    )

    val popularTracks = if (state.activeArtistTracks.isNotEmpty()) state.activeArtistTracks else sampleArtistPopularTracks()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_artist_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Navigation Bar (< Back | ARTIST PROFILE / Artist Name | Share, More >)
            item(key = "artist_top_bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ARTIST PROFILE",
                            style = MaterialTheme.typography.labelSmall,
                            color = onSurfaceMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(Modifier.height(1.dp))
                        Text(
                            text = artist.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                                .clickable { onShareClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = "Share",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp))
                                .clickable { onMoreOptionsClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "More Options",
                                tint = onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 2. Hero Header Card (Dark Studio Artwork with Badges & Text Overlay)
            item(key = "artist_hero_card") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp)
                        .height(260.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(22.dp))
                ) {
                    // Studio background image
                    TrackArtImage(
                        url = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=800&q=80",
                        contentDescription = artist.name,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay for flawless text contrast
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.45f),
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.92f)
                                    )
                                )
                            )
                    )

                    // Top Pills inside Hero
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // REON Featured Artist Pill
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00E5FF))
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "REON Featured Artist",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Format Pill
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "96K / 24-BIT",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Bottom Content Overlay inside Hero
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = artist.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Rounded.Verified,
                                contentDescription = "Verified",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "${state.activeArtistListeners} Monthly Listeners · Tokyo / Berlin",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.5.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Electronic   •   Modular Synth   •   Deep Ambient",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF70C5FF),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            // 3. Action Buttons Row (Follow | Radio | Big Play Button)
            item(key = "artist_action_buttons") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Follow Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (state.isArtistFollowed) onSurface else surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                            .clickable { onFollowToggle() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (state.isArtistFollowed) Icons.Rounded.Check else Icons.Rounded.Add,
                                contentDescription = null,
                                tint = if (state.isArtistFollowed) cardBackground else onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (state.isArtistFollowed) "Following" else "Follow",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (state.isArtistFollowed) cardBackground else onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Radio Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(cardBackground)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                            .clickable { onRadioClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Radio,
                                contentDescription = null,
                                tint = onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Radio",
                                style = MaterialTheme.typography.titleMedium,
                                color = onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Play Button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(onSurface)
                            .clickable {
                                onPlayClick()
                                if (popularTracks.isNotEmpty()) onTrackSelect(popularTracks.first())
                                onShowToast("Playing ${artist.name}")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play Artist",
                            tint = cardBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // 4. REON Master Acoustic Profile Bento Card
            item(key = "artist_acoustic_profile_card") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
                        .clickable { onShowToast("REON Acoustic Calibration: 96kHz / 24-Bit FLAC") }
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MusicNote,
                                contentDescription = null,
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "REON Master Acoustic Profile",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface
                                )
                                Spacer(Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0x1A10B981))
                                        .border(0.5.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "BIT-PERFECT",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF34D399),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(Modifier.height(2.dp))

                            Text(
                                text = "Native 96kHz / 24-Bit FLAC · Dynamic Range 14.2 dB",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.5.sp,
                                color = onSurfaceMuted
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 5. Popular Tracks Section Header
            item(key = "artist_popular_tracks_header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin)
                        .padding(top = 18.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular Tracks",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )

                        Text(
                            text = "See All",
                            style = MaterialTheme.typography.labelMedium,
                            color = onSurfaceMuted,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onShowToast("All tracks by ${artist.name}") }
                        )
                    }

                    Text(
                        text = "Master releases streamed in bit-perfect lossless",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = onSurfaceMuted
                    )
                }
            }

            // 6. Popular Tracks List Items
            itemsIndexed(popularTracks, key = { _, track -> "artist_pop_${track.id}" }) { index, track ->
                val isCurrentPlaying = state.currentTrack.id == track.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 3.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCurrentPlaying) surfaceHigh else cardBackground)
                        .border(
                            ReonSize.hairline,
                            if (isCurrentPlaying) onSurface.copy(alpha = 0.3f) else hairlineColor,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onTrackSelect(track) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rank number or active equalizing icon
                    if (isCurrentPlaying && state.currentTrack.isPlaying) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(onSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.GraphicEq,
                                contentDescription = "Playing",
                                tint = cardBackground,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCurrentPlaying) onSurface else onSurfaceMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.width(26.dp)
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    // Square icon or track art
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = if (isCurrentPlaying) onSurface else onSurfaceMuted,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Track Title & Plays
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = track.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (track.badge.isNotEmpty()) {
                                Spacer(Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(surfaceHigh)
                                        .border(0.5.dp, hairlineColor, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
                                ) {
                                    Text(
                                        text = track.badge,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = onSurfaceMuted,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = track.plays.ifEmpty { "5,412,890 plays" },
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.5.sp,
                            color = onSurfaceMuted
                        )
                    }

                    Text(
                        text = track.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

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

            // 7. Discography & Master Vault Section
            item(key = "artist_discography_header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin)
                        .padding(top = 22.dp, bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Discography & Master Vault",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )

                        Text(
                            text = "Albums",
                            style = MaterialTheme.typography.labelMedium,
                            color = onSurfaceMuted,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onShowToast("Viewing Discography") }
                        )
                    }
                }
            }

            // Horizontal Albums Row
            item(key = "artist_discography_row") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = ReonSpacing.margin),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val sampleAlbums = listOf(
                        AlbumItem("alb_sonic_geom", "Sonic Geometry", "Aurora Glow", "2024", "11 Tracks", "FLAC", artSeed = 1),
                        AlbumItem("alb_aether_res", "Aether Resonance", "Aurora Glow", "2023", "9 Tracks", "24-Bit", artSeed = 2)
                    )

                    sampleAlbums.forEach { album ->
                        Column(
                            modifier = Modifier
                                .width(155.dp)
                                .clickable { onAlbumSelect(album) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(155.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(cardBackground)
                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
                            ) {
                                TrackArtImage(
                                    url = if (album.id == "alb_sonic_geom") "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=500&q=80" else "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&q=80",
                                    contentDescription = album.title,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Top format badge inside card
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = if (album.id == "alb_sonic_geom") "Master 96k" else "Dolby Atmos",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Bottom tag inside card
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(onSurface)
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = if (album.id == "alb_sonic_geom") "Latest LP" else "STUDIO ARCHIVE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = cardBackground,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = album.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "${album.year} · ${album.trackCount}",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.5.sp,
                                color = onSurfaceMuted
                            )
                        }
                    }
                }
            }

            // 8. Editorial Note & Sound Lab Bento Box (Bottom Card)
            item(key = "artist_editorial_card") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 18.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(18.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "EDITORIAL NOTE & SOUND LAB",
                                style = MaterialTheme.typography.labelSmall,
                                color = onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "REON Sonic ID #892",
                                style = MaterialTheme.typography.labelSmall,
                                color = onSurfaceMuted,
                                fontSize = 10.5.sp
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Formed in Berlin and refined through Tokyo's avant-garde analog clubs, ${artist.name} pair vintage Buchla and Eurorack modular synthesizers with pristine 96kHz acoustic spatialization. Their recordings feature unfiltered transient responses and custom-engineered harmonic overtones.",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mastering Vault Status",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurface,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "Verified Bit-Perfect",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF34D399),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun sampleArtistPopularTracks(): List<TrackItem> {
    return listOf(
        TrackItem("tr_ref_1", "Refractions", "Aurora Glow", "Refractions", "04:18", badge = "96k FLAC", plays = "8,924,103 plays", isPlaying = true, artSeed = 1),
        TrackItem("tr_ref_2", "Nightfall Prism", "Aurora Glow", "Refractions", "04:45", badge = "LOSSLESS", plays = "6,412,890 plays", artSeed = 1),
        TrackItem("tr_ref_4", "Electric Horizon", "Aurora Glow", "Refractions", "03:58", badge = "24-BIT", plays = "4,891,012 plays", artSeed = 1),
        TrackItem("tr_ref_3", "Subtle Drift", "Aurora Glow", "Refractions", "05:12", badge = "ATMOS", plays = "3,170,440 plays", artSeed = 1),
        TrackItem("tr_ref_8", "Crystalline", "Aurora Glow", "Refractions", "04:55", badge = "FLAC", plays = "2,852,990 plays", artSeed = 1)
    )
}
