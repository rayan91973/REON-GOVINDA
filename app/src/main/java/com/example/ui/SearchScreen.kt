package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

/**
 * REON — Search Screen
 * Pixel-perfect match to user's uploaded reference UI image:
 * - Clean "Search" top header
 * - Rounded search input bar with search icon and clear 'X'
 * - Filter chips (All, Tracks, Albums, Artists, Playlists, ✦ Hi-Res)
 * - "Recent searches" section with "Clear all" and dismissible tags
 * - "Top result" card with "Best match" overline, large artwork, like button, and solid play FAB
 * - Filtered search results tracklist
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    state: HomeState,
    onSearchChange: (String) -> Unit,
    onFilterSelect: (String) -> Unit,
    onSelectRecentSearch: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onStartVoiceSearch: () -> Unit,
    onCancelVoiceSearch: () -> Unit,
    onClearSearch: () -> Unit,
    onTrackSelect: (TrackItem) -> Unit,
    onArtistFollowToggle: (String) -> Unit,
    onPlaylistSelect: (PlaylistItem) -> Unit = {},
    onAlbumSelect: (AlbumItem) -> Unit = {},
    onArtistSelect: (ArtistItem) -> Unit = {},
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    val filterOptions = listOf("All", "Tracks", "Albums", "Artists", "Playlists", "✦ Hi-Res")
    var selectedFilter by remember { mutableStateOf("All") }

    // Fallback recent searches matching the mockup if state is empty
    val recentSearchesList = remember(state.recentSearches) {
        if (state.recentSearches.isNotEmpty()) {
            state.recentSearches
        } else {
            listOf("Solaris & Kaelen", "Nocturne Trance", "Monolith Sessions", "Chill Electronica")
        }
    }

    // Top result item
    val topResultTrack = remember(state.searchQuery, state.currentTrack) {
        if (state.searchQuery.isNotBlank() && state.searchResultsTracks.isNotEmpty()) {
            state.searchResultsTracks.first()
        } else {
            TrackItem(
                id = "top_res_1",
                title = "Midnight City Lights",
                artist = "Solaris & Kaelen",
                album = "Single",
                duration = "04:18",
                isPlaying = false,
                isLiked = false,
                artSeed = 1
            )
        }
    }

    // Dynamic search results
    val searchResults = remember(state.searchQuery, selectedFilter) {
        sampleSearchResults().filter { track ->
            if (state.searchQuery.isBlank()) true
            else track.title.contains(state.searchQuery, ignoreCase = true) ||
                    track.artist.contains(state.searchQuery, ignoreCase = true) ||
                    track.album.contains(state.searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_search_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 160.dp)
        ) {
            // 1. Search Header Title
            item(key = "search_header_title") {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    modifier = Modifier.padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                )
            }

            // 2. Search Input Bar Container
            item(key = "search_input_bar") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = onSurfaceMuted,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (state.searchQuery.isEmpty()) {
                                Text(
                                    text = "Search songs, artists, albums…",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = onSurfaceMuted,
                                    fontSize = 14.5.sp
                                )
                            }

                            BasicTextField(
                                value = state.searchQuery,
                                onValueChange = onSearchChange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .testTag("search_input"),
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = onSurface,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                cursorBrush = SolidColor(onSurface),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = { keyboardController?.hide() }
                                )
                            )
                        }

                        if (state.searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear search",
                                tint = onSurfaceMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onClearSearch() }
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Mic,
                                contentDescription = "Voice search",
                                tint = onSurfaceMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onStartVoiceSearch() }
                            )
                        }
                    }
                }
            }

            // 3. Filter Chips Horizontal Row (All, Tracks, Albums, Artists, Playlists, ✦ Hi-Res)
            item(key = "search_filter_chips") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { filter ->
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
                                .clickable {
                                    selectedFilter = filter
                                    onFilterSelect(filter)
                                }
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

            // 4. Recent Searches Section (Recent searches + Clear all + flow of tags)
            if (recentSearchesList.isNotEmpty() && state.searchQuery.isEmpty()) {
                item(key = "recent_searches_section") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ReonSpacing.margin, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent searches",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Clear all",
                                style = MaterialTheme.typography.labelMedium,
                                color = onSurfaceMuted,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable { onClearRecentSearches() }
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recentSearchesList.forEach { query ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(cardBackground)
                                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 7.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { onSelectRecentSearch(query) }
                                    ) {
                                        Text(
                                            text = query,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = onSurface,
                                            fontSize = 12.5.sp
                                        )

                                        Spacer(Modifier.width(8.dp))

                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "Remove $query",
                                            tint = onSurfaceMuted,
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clickable { onRemoveRecentSearch(query) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Top Result Section ("Top result" + "Best match" card)
            item(key = "top_result_section") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Top result",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Best match",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            fontSize = 11.5.sp
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Top Result Bento Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(16.dp))
                            .clickable { onTrackSelect(topResultTrack) }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 60dp Artwork
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(surfaceHigh)
                            ) {
                                TrackArtImage(
                                    url = getArtUrlForSeed(topResultTrack.artSeed),
                                    contentDescription = topResultTrack.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            // Metadata (SONG, Title, Artist · Single)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "SONG",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.sp,
                                    color = onSurfaceMuted,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(Modifier.height(2.dp))

                                Text(
                                    text = topResultTrack.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 16.sp
                                )

                                Spacer(Modifier.height(2.dp))

                                Text(
                                    text = "${topResultTrack.artist} • ${topResultTrack.album.ifEmpty { "Single" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = onSurfaceMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp
                                )
                            }

                            // Actions: Heart & Solid Black Play FAB
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { onShowToast("Liked ${topResultTrack.title}") },
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = onSurfaceMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(primaryColor)
                                        .clickable { onTrackSelect(topResultTrack) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = "Play",
                                        tint = onPrimaryColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Search Results Tracklist Header & Items
            item(key = "search_results_header") {
                Text(
                    text = if (state.searchQuery.isNotEmpty()) "Tracks" else "Explore Master Collection",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(
                        start = ReonSpacing.margin,
                        end = ReonSpacing.margin,
                        top = 14.dp,
                        bottom = 6.dp
                    )
                )
            }

            items(searchResults, key = { it.id }) { track ->
                val isPlaying = state.currentTrack.id == track.id && state.currentTrack.isPlaying

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 3.dp)
                        .clip(RoundedCornerShape(ReonRadius.md))
                        .background(if (isPlaying) surfaceHigh else cardBackground)
                        .border(
                            ReonSize.hairline,
                            if (isPlaying) onSurface.copy(alpha = 0.2f) else hairlineColor,
                            RoundedCornerShape(ReonRadius.md)
                        )
                        .clickable { onTrackSelect(track) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(ReonRadius.sm))
                    ) {
                        TrackArtImage(
                            url = getArtUrlForSeed(track.artSeed),
                            contentDescription = track.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isPlaying) FontWeight.SemiBold else FontWeight.Medium,
                            color = onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = track.artist,
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Text(
                                text = " • ",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceMuted
                            )
                            Text(
                                text = track.duration,
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted
                            )
                        }
                    }

                    // Lossless FLAC Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(ReonRadius.xs))
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(ReonRadius.xs))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (track.badge.isNotEmpty()) track.badge else "FLAC",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = onSurfaceMuted
                        )
                    }

                    Spacer(Modifier.width(4.dp))

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

private fun sampleSearchResults(): List<TrackItem> {
    return listOf(
        TrackItem("sr_1", "Midnight City Lights", "Solaris & Kaelen", "Single", "04:18", isLiked = true, isPlaying = true, artSeed = 1),
        TrackItem("sr_2", "Nocturne Trance Sessions", "Nocturne", "Nocturne", "06:45", isLiked = false, artSeed = 2),
        TrackItem("sr_3", "Refractions (Master Edit)", "Aurora Glow", "Refractions", "04:18", isLiked = true, artSeed = 3),
        TrackItem("sr_4", "Monolith Sessions 002", "Monolith Archive", "Sessions", "07:12", isLiked = false, artSeed = 4),
        TrackItem("sr_5", "Chill Electronica Waves", "REON Focus", "Chillout", "05:30", isLiked = true, artSeed = 5),
        TrackItem("sr_6", "Nightcall (Neon Re-edit)", "Kavinsky", "OutRun", "04:45", isLiked = true, artSeed = 6)
    )
}

private fun getArtUrlForSeed(seed: Int): String {
    return when (seed % 6) {
        1 -> "https://picsum.photos/seed/reon_refractions/300/300"
        2 -> "https://picsum.photos/seed/reon_nightfall/300/300"
        3 -> "https://picsum.photos/seed/reon_nightcall/300/300"
        4 -> "https://picsum.photos/seed/reon_usb002/300/300"
        5 -> "https://picsum.photos/seed/reon_chroma/300/300"
        else -> "https://picsum.photos/seed/reon_theta/300/300"
    }
}
