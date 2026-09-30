package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.PlaylistPlay
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Airplay
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FileDownloadDone
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Speaker
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.data.MusicTrack
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * Tactile micro-bounce modifier
 */
@Composable
fun Modifier.tactileClick(
    scaleDown: Float = 0.94f,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
        label = "tactile_scale"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
            onClick = onClick
        )
}

/**
 * REON — Now Playing Screen
 * Designed precisely to match the user's reference mockup:
 * - Top header with "PLAYING FROM PLAYLIST" and playlist title
 * - Full-width high-res album artwork with 20dp rounded corners and soft drop shadow
 * - Track title, artist subtitle with "LOSSLESS 24-BIT" pill, and Heart & Bookmark icons
 * - Clean linear scrubber bar with elapsed and remaining time codes
 * - 5-button transport controls with 64dp solid primary Play/Pause button
 * - "AirPlay / Speaker" device output pill with quick actions (Download, Queue, Share)
 * - Synchronized lyrics preview card with "Full View" toggle
 * - "Up Next" queue section with count pill, "View Queue" trigger, and stack of upcoming track cards
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    state: NowPlayingState,
    onBack: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onSeek: (Float) -> Unit = {},
    onShuffle: () -> Unit = {},
    onRepeat: () -> Unit = {},
    onLike: () -> Unit = {},
    onMenuToggle: () -> Unit = {},
    onMenuClose: () -> Unit = {},
    onDownload: () -> Unit = {},
    onQueueClick: () -> Unit = {},
    onPlaylistClick: () -> Unit = {},
    onAddToPlaylist: (String) -> Unit = {},
    onPlayNextMenu: () -> Unit = {},
    onAddToQueue: () -> Unit = {},
    onViewAlbum: () -> Unit = {},
    onShareTrack: () -> Unit = {},
    onSleepTimerClick: () -> Unit = {},
    onTrackSelect: (MusicTrack) -> Unit = {},
    onExpandPlayer: () -> Unit = {},
    onDismissToast: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val isOled = MaterialTheme.reonExtras.isOled

    var isLyricsFullView by remember { mutableStateOf(false) }

    // Fallback sample Up Next tracks if queue is empty
    val upNextTracks = remember(state.queueTracks) {
        if (state.queueTracks.isNotEmpty()) {
            state.queueTracks.filter { it.id != state.currentTrack.id }.take(3)
        } else {
            listOf(
                MusicTrack(
                    id = "up_next_1",
                    title = "Neon Horizons",
                    artist = "Aura Sound",
                    album = "Late Night Resonance",
                    durationMs = 314000L,
                    albumArtUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&q=80"
                ),
                MusicTrack(
                    id = "up_next_2",
                    title = "Subtle Echoes",
                    artist = "Mirage Architecture",
                    album = "Late Night Resonance",
                    durationMs = 232000L,
                    albumArtUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=300&q=80"
                ),
                MusicTrack(
                    id = "up_next_3",
                    title = "Crystalline Dispersion",
                    artist = "Kaelen Solo Archive",
                    album = "Late Night Resonance",
                    durationMs = 287000L,
                    albumArtUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&q=80"
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .testTag("now_playing_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            // 1. Top Header (< Down Chevron | PLAYING FROM PLAYLIST / Late Night Resonance | More ... >)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Downward Dismiss Chevron
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .tactileClick(onClick = onBack)
                        .testTag("back_button"),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Dismiss",
                        tint = onSurface,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Center Title
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PLAYING FROM PLAYLIST",
                        style = ReonTokens.LabelMono,
                        color = onSurfaceMuted,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (state.album.isNotEmpty()) state.album else "Late Night Resonance",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // More Options Trigger
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .tactileClick(onClick = onMenuToggle)
                        .testTag("menu_button"),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreHoriz,
                        contentDescription = "More Options",
                        tint = onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // 2. Large High-Res Artwork Card (Square with 20dp rounded corners & soft shadow)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .then(
                        if (isOled) Modifier else Modifier.shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0x18000000),
                            spotColor = Color(0x28000000)
                        )
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(surfaceHigh)
            ) {
                if (state.albumArtUrl.isNotEmpty()) {
                    AsyncImage(
                        model = state.albumArtUrl,
                        contentDescription = state.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.art_refractions),
                        contentDescription = state.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 3. Track Title, Artist, Lossless Badge & Heart / Bookmark Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.title.ifEmpty { "Midnight City Lights" },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 22.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = state.artist.ifEmpty { "Solaris & Kaelen" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceMuted,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "·",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceMuted,
                            fontSize = 14.sp
                        )

                        // LOSSLESS 24-BIT Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LOSSLESS 24-BIT",
                                style = ReonTokens.LabelMono,
                                color = onSurface,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Heart Like & Bookmark Actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onLike,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = "Like",
                            tint = onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = onPlaylistClick,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.BookmarkBorder,
                            contentDescription = "Save / Playlist",
                            tint = onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // 4. Interactive Scrubber / Seek Bar
            LinearScrubberBar(
                progress = state.progress,
                onSeek = onSeek,
                elapsedText = state.positionLabel.ifEmpty { "1:45" },
                remainingText = state.remainingLabel.ifEmpty { "-2:33" }
            )

            Spacer(Modifier.height(14.dp))

            // 5. Main Playback Controls Row (Shuffle, Previous, 64dp Solid Play/Pause, Next, Repeat)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle Button
                IconButton(
                    onClick = onShuffle,
                    modifier = Modifier.size(48.dp).testTag("shuffle_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (state.shuffleOn) onSurface else onSurfaceMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Previous Button
                IconButton(
                    onClick = onPrevious,
                    modifier = Modifier.size(48.dp).testTag("previous_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "Previous",
                        tint = onSurface,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Play / Pause Button (64dp solid rounded-square)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .then(
                            if (isOled) Modifier else Modifier.shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color(0x18000000),
                                spotColor = Color(0x24000000)
                            )
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(primaryColor)
                        .tactileClick(scaleDown = 0.92f, onClick = onPlayPause)
                        .testTag("play_pause_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        tint = onPrimaryColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Next Button
                IconButton(
                    onClick = onNext,
                    modifier = Modifier.size(48.dp).testTag("next_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "Next",
                        tint = onSurface,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Repeat Button
                IconButton(
                    onClick = onRepeat,
                    modifier = Modifier.size(48.dp).testTag("repeat_button")
                ) {
                    Icon(
                        imageVector = if (state.repeatOn) Icons.Rounded.RepeatOne else Icons.Rounded.Repeat,
                        contentDescription = "Repeat",
                        tint = if (state.repeatOn) onSurface else onSurfaceMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // 6. Device Output & Utility Bar (AirPlay / Speaker + Download, Queue, Share)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardBackground)
                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Device selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onSleepTimerClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Airplay,
                            contentDescription = "Output device",
                            tint = onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = state.audioOutputDevice.ifEmpty { "AirPlay / Speaker" },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = onSurface
                        )
                    }

                    // Right: Actions (Download, Queue, Share)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Download
                        Icon(
                            imageVector = if (state.isDownloaded) Icons.Rounded.FileDownloadDone else Icons.Rounded.Download,
                            contentDescription = "Download",
                            tint = onSurface,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onDownload() }
                        )

                        // Queue
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.QueueMusic,
                            contentDescription = "Queue",
                            tint = onSurface,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onQueueClick() }
                        )

                        // Share
                        Icon(
                            imageVector = Icons.Rounded.IosShare,
                            contentDescription = "Share",
                            tint = onSurface,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onShareTrack() }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 7. Lyrics Card with "Full View" toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBackground)
                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LYRICS",
                            style = ReonTokens.LabelMono,
                            color = onSurfaceMuted,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = if (isLyricsFullView) "Collapse" else "Full View",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = onSurface,
                            modifier = Modifier.clickable { isLyricsFullView = !isLyricsFullView }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    if (!isLyricsFullView) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Static humming across the skyline",
                                style = MaterialTheme.typography.bodyMedium,
                                color = onSurfaceMuted,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Reflections shatter on the obsidian glass",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Synchronized pulses through the dark",
                                style = MaterialTheme.typography.bodyMedium,
                                color = onSurfaceMuted,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Static humming across the skyline", style = MaterialTheme.typography.bodyMedium, color = onSurfaceMuted, fontSize = 14.sp)
                            Text(text = "Reflections shatter on the obsidian glass", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 15.sp)
                            Text(text = "Synchronized pulses through the dark", style = MaterialTheme.typography.bodyMedium, color = onSurfaceMuted, fontSize = 14.sp)
                            Text(text = "Deep frequencies resonate in timeless space", style = MaterialTheme.typography.bodyMedium, color = onSurfaceMuted, fontSize = 14.sp)
                            Text(text = "Pure lossless waves bridging the divide", style = MaterialTheme.typography.bodyMedium, color = onSurfaceMuted, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 8. Up Next Section (Header + Stack of Cards)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Up Next",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = onSurface,
                        fontSize = 17.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(surfaceHigh)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${upNextTracks.size}",
                            style = ReonTokens.LabelMono,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = onSurfaceMuted
                        )
                    }
                }

                Text(
                    text = "View Queue",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = onSurface,
                    modifier = Modifier.clickable { onQueueClick() }
                )
            }

            Spacer(Modifier.height(10.dp))

            // Up Next Cards Stack
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                upNextTracks.forEach { track ->
                    UpNextTrackCard(
                        track = track,
                        onClick = { onTrackSelect(track) }
                    )
                }
            }

            Spacer(Modifier.height(36.dp))
        }

        // --- Modals & Sheets ---

        // Queue Modal Bottom Sheet
        if (state.isQueueExpanded) {
            val queueSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = onQueueClick,
                sheetState = queueSheetState,
                shape = RoundedCornerShape(topStart = ReonRadius.sheet, topEnd = ReonRadius.sheet),
                containerColor = cardBackground,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            ) {
                QueueModalSheetContent(
                    state = state,
                    onTrackSelect = { track ->
                        onTrackSelect(track)
                        onQueueClick()
                    },
                    onClose = onQueueClick
                )
            }
        }

        // Playlist Selection Modal Bottom Sheet
        if (state.isPlaylistDialogOpen) {
            val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = onPlaylistClick,
                sheetState = playlistSheetState,
                shape = RoundedCornerShape(topStart = ReonRadius.sheet, topEnd = ReonRadius.sheet),
                containerColor = cardBackground,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            ) {
                PlaylistModalSheetContent(
                    trackTitle = state.title,
                    onSelectPlaylist = { playlistName ->
                        onAddToPlaylist(playlistName)
                    },
                    onClose = onPlaylistClick
                )
            }
        }

        // Options Context Menu Bottom Sheet (matching reference UI)
        if (state.isMenuOpen) {
            val menuSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = onMenuClose,
                sheetState = menuSheetState,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                containerColor = cardBackground,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 24.dp)
                ) {
                    // Track Header Row (Artwork + Title + Artist)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                        ) {
                            if (state.albumArtUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = state.albumArtUrl,
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(R.drawable.art_refractions),
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.title.ifEmpty { "Midnight City Lights" },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = state.artist.ifEmpty { "Solaris & Kaelen" },
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 13.sp
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = ReonSize.hairline,
                        color = hairlineColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 1. Play Next
                    OptionsSheetRowItem(
                        icon = Icons.AutoMirrored.Rounded.PlaylistPlay,
                        title = "Play Next",
                        onClick = {
                            onMenuClose()
                            onPlayNextMenu()
                        }
                    )

                    // 2. Add to Queue
                    OptionsSheetRowItem(
                        icon = Icons.AutoMirrored.Rounded.QueueMusic,
                        title = "Add to Queue",
                        onClick = {
                            onMenuClose()
                            onAddToQueue()
                        }
                    )

                    // 3. View Album
                    OptionsSheetRowItem(
                        icon = Icons.Rounded.Album,
                        title = "View Album",
                        onClick = {
                            onMenuClose()
                            onViewAlbum()
                        }
                    )

                    // 4. Go to Artist
                    OptionsSheetRowItem(
                        icon = Icons.Rounded.PersonOutline,
                        title = "Go to Artist",
                        onClick = {
                            onMenuClose()
                            onViewAlbum() // Navigates to artist/album context
                        }
                    )

                    // 5. Share Track
                    OptionsSheetRowItem(
                        icon = Icons.Rounded.IosShare,
                        title = "Share Track",
                        onClick = {
                            onMenuClose()
                            onShareTrack()
                        }
                    )

                    // 6. Track Details
                    OptionsSheetRowItem(
                        icon = Icons.Rounded.Info,
                        title = "Track Details",
                        onClick = {
                            onMenuClose()
                            onDownload()
                        }
                    )

                    Spacer(Modifier.height(14.dp))

                    // Full-width "Close" Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(surfaceHigh)
                            .clickable(onClick = onMenuClose),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Close",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = onSurface,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Clean Scrubber Bar with Solid Black Progress Line, Circular Drag Thumb, and Monospace Time Stamps
 */
@Composable
private fun LinearScrubberBar(
    progress: Float,
    onSeek: (Float) -> Unit,
    elapsedText: String,
    remainingText: String
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val trackBg = MaterialTheme.colorScheme.surfaceContainerHigh
    val isOled = MaterialTheme.reonExtras.isOled

    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(progress) }

    val currentProgress = if (isDragging) dragProgress else progress.coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newProgress = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                        onSeek(newProgress)
                    }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            isDragging = true
                            dragProgress = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                        },
                        onDragEnd = {
                            isDragging = false
                            onSeek(dragProgress)
                        },
                        onDragCancel = {
                            isDragging = false
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val width = size.width.toFloat()
                            if (width > 0) {
                                dragProgress = (dragProgress + dragAmount / width).coerceIn(0f, 1f)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            val fullWidth = maxWidth

            // Background Unplayed Track (4dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(trackBg)
            )

            // Foreground Played Track (4dp height)
            Box(
                modifier = Modifier
                    .width(fullWidth * currentProgress)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(onSurface)
            )

            // Draggable Solid Black Thumb
            val thumbOffset = (fullWidth * currentProgress) - 7.dp
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset.coerceAtLeast(0.dp))
                    .size(14.dp)
                    .then(
                        if (isOled) Modifier else Modifier.shadow(
                            elevation = 3.dp,
                            shape = CircleShape,
                            ambientColor = Color(0x20000000),
                            spotColor = Color(0x30000000)
                        )
                    )
                    .clip(CircleShape)
                    .background(onSurface)
            )
        }

        // Time Stamps (Elapsed on left, Negative Remaining on right)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = elapsedText,
                style = ReonTokens.LabelMono,
                fontSize = 12.sp,
                color = onSurface
            )

            Text(
                text = remainingText,
                style = ReonTokens.LabelMono,
                fontSize = 12.sp,
                color = onSurfaceMuted
            )
        }
    }
}

/**
 * Up Next Track Card
 */
@Composable
private fun UpNextTrackCard(
    track: MusicTrack,
    onClick: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground)
            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 44dp Artwork
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                if (track.albumArtUrl.isNotEmpty()) {
                    AsyncImage(
                        model = track.albumArtUrl,
                        contentDescription = track.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.art_refractions),
                        contentDescription = track.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Track info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
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
                text = track.durationLabel,
                style = ReonTokens.LabelMono,
                color = onSurfaceMuted,
                fontSize = 12.sp
            )

            Spacer(Modifier.width(10.dp))

            // Reorder drag handle icon (= two lines)
            Icon(
                imageVector = Icons.Rounded.DensityMedium,
                contentDescription = "Reorder",
                tint = onSurfaceMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun MenuRowItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = onSurface,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = onSurface
        )
    }
}

@Composable
private fun QueueModalSheetContent(
    state: NowPlayingState,
    onTrackSelect: (MusicTrack) -> Unit,
    onClose: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "UP NEXT IN QUEUE",
                    style = ReonTokens.LabelMono,
                    color = onSurfaceMuted
                )
                Text(
                    text = "${state.queueTracks.size} Tracks Scheduled",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = onSurface
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            items(state.queueTracks) { track ->
                val isCurrent = track.id == state.currentTrack.id
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ReonSize.trackRowHeight)
                        .clickable { onTrackSelect(track) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(ReonSize.trackArtRow)
                            .clip(RoundedCornerShape(ReonRadius.md))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.art_refractions),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Medium,
                            color = onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${track.artist} · ${track.durationLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(ReonRadius.xs))
                            .border(ReonSize.hairline, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(ReonRadius.xs))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "FLAC",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = onSurfaceMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaylistModalSheetContent(
    trackTitle: String,
    onSelectPlaylist: (String) -> Unit,
    onClose: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val playlists = listOf("Late Night Resonance", "Deep Focus // Electric Light", "Hi-Res Studio Master", "Spatial Ambience")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ADD TO PLAYLIST",
                    style = ReonTokens.LabelMono,
                    color = onSurfaceMuted
                )
                Text(
                    text = "\"$trackTitle\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            playlists.forEach { playlistName ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { onSelectPlaylist(playlistName); onClose() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = playlistName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = onSurface
                    )
                    Icon(
                        imageVector = Icons.Rounded.PlaylistAdd,
                        contentDescription = "Add",
                        tint = onSurfaceMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = ReonSize.hairline)
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun OptionsSheetRowItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = onSurface,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 14.5.sp,
            color = onSurface
        )
    }
}
