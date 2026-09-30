package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * REON — Home Screen
 * Implements the full Electric Horizon Design System with 18 consecutive sections
 * in a high-performance single root LazyColumn, rich Search Tab, Downloads Tab,
 * floating Mini Player dock, and 3-tab floating bottom navigation.
 */
@Composable
fun HomeScreen(
    state: HomeState,
    onSearchChange: (String) -> Unit = {},
    onFilterSelect: (String) -> Unit = {},
    onSelectRecentSearch: (String) -> Unit = {},
    onRemoveRecentSearch: (String) -> Unit = {},
    onClearRecentSearches: () -> Unit = {},
    onStartVoiceSearch: () -> Unit = {},
    onCancelVoiceSearch: () -> Unit = {},
    onClearSearch: () -> Unit = {},
    onMoodSelect: (String) -> Unit = {},
    onPlayPause: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    onLike: () -> Unit = {},
    onTrackSelect: (TrackItem) -> Unit = {},
    onArtistFollowToggle: (String) -> Unit = {},
    onOpenNowPlaying: () -> Unit = {},
    onTabSelected: (HomeTab) -> Unit = {},
    onDownloadAll: () -> Unit = {},
    onRemoveDownload: (String) -> Unit = {},
    onToggleOfflineMode: () -> Unit = {},
    onToggleAutoSync: () -> Unit = {},
    onToggleCellularDownload: () -> Unit = {},
    onDownloadFilterSelect: (String) -> Unit = {},
    onDownloadQualitySelect: (String) -> Unit = {},
    onToggleQualitySelector: (Boolean) -> Unit = {},
    onShuffleDownloads: () -> Unit = {},
    onClearAllDownloads: () -> Unit = {},
    onPlaylistLikeToggle: () -> Unit = {},
    onPlaylistDownloadToggle: () -> Unit = {},
    onPlaylistShuffle: () -> Unit = {},
    onPlaylistPlayAll: () -> Unit = {},
    onOpenPlaylist: (PlaylistItem?) -> Unit = {},
    onClosePlaylist: () -> Unit = {},
    onAlbumLikeToggle: () -> Unit = {},
    onAlbumDownloadToggle: () -> Unit = {},
    onAlbumShuffle: () -> Unit = {},
    onAlbumPlayAll: () -> Unit = {},
    onOpenAlbum: (AlbumItem?) -> Unit = {},
    onCloseAlbum: () -> Unit = {},
    onOpenArtist: (ArtistItem?) -> Unit = {},
    onCloseArtist: () -> Unit = {},
    onArtistFollowProfileToggle: () -> Unit = {},
    onOpenLikedSongs: () -> Unit = {},
    onCloseLikedSongs: () -> Unit = {},
    onOpenDownloads: () -> Unit = {},
    onCloseDownloads: () -> Unit = {},
    onOpenHistory: () -> Unit = {},
    onCloseHistory: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onCloseSettings: () -> Unit = {},
    onOpenAnalytics: () -> Unit = {},
    onCloseAnalytics: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    onCloseNotifications: () -> Unit = {},
    onMarkNotificationAsRead: (String) -> Unit = {},
    onMarkAllNotificationsAsRead: () -> Unit = {},
    onClearAllNotifications: () -> Unit = {},
    onDeleteNotification: (String) -> Unit = {},
    onUpdateProfile: (String, String) -> Unit = { _, _ -> },
    onOpenEditProfile: () -> Unit = {},
    onCloseEditProfile: () -> Unit = {},
    onOpenAbout: () -> Unit = {},
    onCloseAbout: () -> Unit = {},
    onOpenLicenses: () -> Unit = {},
    onCloseLicenses: () -> Unit = {},
    onSetThemeMode: (String) -> Unit = {},
    onSetAccentColor: (Int) -> Unit = {},
    onSetBackgroundTheme: (Int) -> Unit = {},
    onSetFontFamily: (String) -> Unit = {},
    onSetFontSizeScale: (Float) -> Unit = {},
    onClearCache: () -> Unit = {},
    onOptimizeThumbnails: () -> Unit = {},
    onShowToast: (String) -> Unit = {},
    onDismissToast: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val listState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ReonTokens.Canvas)
            .statusBarsPadding()
            .testTag("reon_home_screen")
    ) {
        if (state.isNotificationsOpen) {
            NotificationScreen(
                state = state,
                onBackClick = onCloseNotifications,
                onTrackSelect = onTrackSelect,
                onOpenSettings = onOpenSettings,
                onOpenDownloads = { onTabSelected(HomeTab.Library) },
                onMarkAsRead = onMarkNotificationAsRead,
                onMarkAllAsRead = onMarkAllNotificationsAsRead,
                onClearAll = onClearAllNotifications,
                onDeleteNotification = onDeleteNotification
            )
        } else if (state.isAnalyticsOpen) {
            AnalyticsScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onBackClick = onCloseAnalytics,
                onPlayPause = onPlayPause,
                onLike = onLike,
                onOpenNowPlaying = onOpenNowPlaying,
                onShowToast = onShowToast
            )
        } else if (state.isSettingsOpen) {
            SettingsScreen(
                state = state,
                onBackClick = onCloseSettings,
                onShareClick = {
                    ShareHelper.shareApp(context)
                    onShowToast("Shared REON Audio Engine Configuration")
                },
                onUpdateProfile = onUpdateProfile,
                onOpenEditProfile = onOpenEditProfile,
                onCloseEditProfile = onCloseEditProfile,
                onOpenAbout = onOpenAbout,
                onCloseAbout = onCloseAbout,
                onOpenLicenses = onOpenLicenses,
                onCloseLicenses = onCloseLicenses,
                onSetThemeMode = onSetThemeMode,
                onSetAccentColor = onSetAccentColor,
                onSetBackgroundTheme = onSetBackgroundTheme,
                onSetFontFamily = onSetFontFamily,
                onSetFontSizeScale = onSetFontSizeScale,
                onClearCache = onClearCache,
                onOptimizeThumbnails = onOptimizeThumbnails,
                onShowToast = onShowToast
            )
        } else if (state.isHistoryOpen) {
            HistoryScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onBackClick = onCloseHistory,
                onShareClick = {
                    ShareHelper.shareApp(context)
                    onShowToast("Shared Listening History Timeline")
                },
                onClearHistoryClick = { onShowToast("Listening History Cleared") },
                onToggleLike = { id -> onLike() },
                onShowToast = onShowToast
            )
        } else if (state.isLikedSongsOpen) {
            LikedSongsScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onBackClick = onCloseLikedSongs,
                onShareClick = {
                    ShareHelper.sharePlaylist(context, "Liked Masterpieces")
                    onShowToast("Shared Liked Masterpieces Library")
                },
                onPlayAllClick = { onShowToast("Playing Liked Songs") },
                onToggleLike = { track -> onLike() },
                onShowToast = onShowToast
            )
        } else if (state.isDownloadsOpen) {
            DownloadsScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onBackClick = onCloseDownloads,
                onDownloadAll = onDownloadAll,
                onRemoveDownload = onRemoveDownload,
                onToggleOfflineMode = onToggleOfflineMode,
                onToggleAutoSync = onToggleAutoSync,
                onToggleCellular = onToggleCellularDownload,
                onFilterSelect = onDownloadFilterSelect,
                onQualitySelect = onDownloadQualitySelect,
                onToggleQualitySelector = onToggleQualitySelector,
                onShuffleAll = onShuffleDownloads,
                onClearAll = onClearAllDownloads,
                onOpenPlaylist = onOpenPlaylist,
                onOpenAlbum = onOpenAlbum,
                onOpenArtist = onOpenArtist,
                onOpenLikedSongs = onOpenLikedSongs,
                onShowToast = onShowToast
            )
        } else if (state.selectedArtist != null) {
            ArtistScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onAlbumSelect = onOpenAlbum,
                onBackClick = onCloseArtist,
                onShareClick = {
                    ShareHelper.shareArtist(context, state.activeArtistName)
                    onShowToast("Shared '${state.activeArtistName}' profile")
                },
                onFollowToggle = onArtistFollowProfileToggle,
                onRadioClick = { onShowToast("${state.activeArtistName} Radio Started") },
                onPlayClick = { onShowToast("Playing ${state.activeArtistName}") },
                onMoreOptionsClick = { onShowToast("Artist options") },
                onShowToast = onShowToast
            )
        } else if (state.selectedAlbum != null) {
            AlbumScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onBackClick = onCloseAlbum,
                onShareClick = {
                    ShareHelper.shareAlbum(context, state.activeAlbumTitle, state.activeArtistName)
                    onShowToast("Shared '${state.activeAlbumTitle}' album")
                },
                onLikeToggle = onAlbumLikeToggle,
                onDownloadToggle = onAlbumDownloadToggle,
                onShuffleClick = onAlbumShuffle,
                onPlayAllClick = onAlbumPlayAll,
                onMoreOptionsClick = { onShowToast("Album options") },
                onShowToast = onShowToast
            )
        } else if (state.selectedPlaylist != null) {
            PlaylistScreen(
                state = state,
                onTrackSelect = onTrackSelect,
                onBackClick = onClosePlaylist,
                onShareClick = {
                    ShareHelper.sharePlaylist(context, state.activePlaylistTitle)
                    onShowToast("Shared ${state.activePlaylistTitle} playlist")
                },
                onLikeToggle = onPlaylistLikeToggle,
                onDownloadToggle = onPlaylistDownloadToggle,
                onShuffleClick = onPlaylistShuffle,
                onPlayAllClick = onPlaylistPlayAll,
                onMoreOptionsClick = { onShowToast("Playlist options") },
                onShowToast = onShowToast
            )
        } else {
            when (state.currentTab) {
                HomeTab.Search -> {
                    SearchScreen(
                        state = state,
                        onSearchChange = onSearchChange,
                        onFilterSelect = onFilterSelect,
                        onSelectRecentSearch = onSelectRecentSearch,
                        onRemoveRecentSearch = onRemoveRecentSearch,
                        onClearRecentSearches = onClearRecentSearches,
                        onStartVoiceSearch = onStartVoiceSearch,
                        onCancelVoiceSearch = onCancelVoiceSearch,
                        onClearSearch = onClearSearch,
                        onTrackSelect = onTrackSelect,
                        onArtistFollowToggle = onArtistFollowToggle,
                        onPlaylistSelect = onOpenPlaylist,
                        onAlbumSelect = onOpenAlbum,
                        onArtistSelect = onOpenArtist,
                        onShowToast = onShowToast
                    )
                }
                HomeTab.Library -> {
                    LibraryScreen(
                        state = state,
                        onTrackSelect = onTrackSelect,
                        onOpenLikedSongs = onOpenLikedSongs,
                        onOpenHistory = onOpenHistory,
                        onOpenDownloads = onOpenDownloads,
                        onOpenPlaylist = onOpenPlaylist,
                        onOpenAlbum = onOpenAlbum,
                        onOpenArtist = onOpenArtist,
                        onOpenNotifications = onOpenNotifications,
                        onOpenAnalytics = onOpenAnalytics,
                        onOpenSettings = onOpenSettings,
                        onShowToast = onShowToast
                    )
                }
                HomeTab.Analytics -> {
                    AnalyticsScreen(
                        state = state,
                        onTrackSelect = onTrackSelect,
                        onBackClick = { onTabSelected(HomeTab.Home) },
                        onPlayPause = onPlayPause,
                        onLike = onLike,
                        onOpenNowPlaying = onOpenNowPlaying,
                        onShowToast = onShowToast
                    )
                }
                HomeTab.Settings -> {
                    SettingsScreen(
                        state = state,
                        onBackClick = { onTabSelected(HomeTab.Home) },
                        onShareClick = { onShowToast("Shared REON Audio Engine") },
                        onUpdateProfile = onUpdateProfile,
                        onOpenEditProfile = onOpenEditProfile,
                        onCloseEditProfile = onCloseEditProfile,
                        onOpenAbout = onOpenAbout,
                        onCloseAbout = onCloseAbout,
                        onOpenLicenses = onOpenLicenses,
                        onCloseLicenses = onCloseLicenses,
                        onSetThemeMode = onSetThemeMode,
                        onSetAccentColor = onSetAccentColor,
                        onSetBackgroundTheme = onSetBackgroundTheme,
                        onSetFontFamily = onSetFontFamily,
                        onSetFontSizeScale = onSetFontSizeScale,
                        onClearCache = onClearCache,
                        onOptimizeThumbnails = onOptimizeThumbnails,
                        onShowToast = onShowToast
                    )
                }
                HomeTab.Home -> {
                    var selectedHomeCategory by remember { mutableStateOf("All") }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 4.dp, bottom = 180.dp)
                    ) {
                        // 1. Top Bar (REON [STUDIO] | Notifications, Tune, Avatar)
                        item(key = "section_top_bar") {
                            HomeTopBar(
                                onAnalyticsClick = onOpenAnalytics,
                                onNotificationClick = onOpenNotifications,
                                onFiltersClick = { onShowToast("Filter Audio Options") },
                                onProfileClick = { onTabSelected(HomeTab.Settings) }
                            )
                        }

                        // 2. Greeting Header Block (TUESDAY, OCTOBER 24 | ● LOSSLESS 24-BIT | Good evening)
                        item(key = "section_greeting_header") {
                            HomeGreetingHeader()
                        }

                        // 3. Category Filter Chips (All, Playlists, Albums, Artists, Hi-Res)
                        item(key = "section_category_chips") {
                            HomeCategoryChips(
                                selectedCategory = selectedHomeCategory,
                                onCategorySelect = { selectedHomeCategory = it }
                            )
                        }

                        // 4. Featured Release Hero Card (Nocturne Sessions Vol. IV)
                        item(key = "section_hero_residency") {
                            HomeHeroAcousticResidencyCard(
                                onStreamMasterClick = {
                                    onTrackSelect(
                                        TrackItem(
                                            id = "nt_4",
                                            title = "Nocturne Sessions Vol. IV",
                                            artist = "Solaris & Kaelen",
                                            album = "Nocturne Sessions",
                                            duration = "5:20",
                                            badge = "24-BIT",
                                            artSeed = 1
                                        )
                                    )
                                    onShowToast("Playing Nocturne Sessions Vol. IV")
                                },
                                onAddClick = { onShowToast("Added to Library") },
                                onMoreClick = { onShowToast("Album Options") }
                            )
                        }

                        // 5. Jump Back In (2x2 Grid with Progress Bars)
                        item(key = "section_jump_back_in") {
                            HomeJumpBackInGrid(
                                onTrackSelect = onTrackSelect,
                                onSeeAllClick = { onShowToast("Viewing All Recent Listening") }
                            )
                        }

                        // 6. Curated Playlists (Late Night Resonance, Architectural Lows)
                        item(key = "section_curated_vaults") {
                            HomeCuratedVaultsRow(
                                onVaultSelect = { vault ->
                                    onOpenPlaylist(
                                        PlaylistItem(
                                            id = "pl_v1",
                                            title = if (vault == "all") "Curated Playlists" else vault,
                                            subtitle = "Deep spatial soundscapes & modular works",
                                            trackCount = "28 tracks",
                                            duration = "2h 42m",
                                            artSeed = 1
                                        )
                                    )
                                },
                                onSeeAllClick = { onShowToast("Viewing All 18 Playlists") }
                            )
                        }

                        // 7. Quick Picks (Vertical track list with circular play triggers)
                        item(key = "section_heavy_rotation") {
                            HomeHeavyRotationList(
                                onTrackSelect = onTrackSelect,
                                onPlayAllClick = {
                                    onTrackSelect(
                                        TrackItem("qp_1", "Crystalline Dispersion", "Solaris & Aura Lab", "Quick", "6:42", badge = "LOSSLESS", artSeed = 1)
                                    )
                                    onShowToast("Playing Quick Picks Queue")
                                },
                                onShowToast = onShowToast
                            )
                        }

                        // 8. Artists You May Like (Solaris & Kaelen, Aura Sound Lab)
                        item(key = "section_artists_in_residence") {
                            HomeArtistsInResidenceRow(
                                onArtistSelect = { artistName ->
                                    onOpenArtist(
                                        ArtistItem(
                                            id = "art_1",
                                            name = artistName,
                                            genre = "Electronic",
                                            isFollowing = true,
                                            artSeed = 1
                                        )
                                    )
                                },
                                onFollowClick = { onShowToast("Followed $it") },
                                onExploreClick = { onShowToast("Exploring Featured Artists") }
                            )
                        }
                    }
                }
            }
        }

        // 19. Floating Mini Player & Upside Floating Toast notification
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Toast notification pill floating just upside of mini player
            AnimatedVisibility(
                visible = state.toastMessage != null,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                state.toastMessage?.let { msg ->
                    LaunchedEffect(msg) {
                        kotlinx.coroutines.delay(2600)
                        onDismissToast()
                    }

                    Box(
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
                            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.2f))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0B1020))
                            .border(1.dp, Color(0xFF242C44), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = msg,
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            if (state.isMiniPlayerVisible) {
                MiniPlayerDock(
                    track = state.currentTrack,
                    isPlaying = state.currentTrack.isPlaying,
                    isLiked = state.currentTrack.isLiked,
                    onExpand = onOpenNowPlaying,
                    onPlayPause = onPlayPause,
                    onPrevious = onPrevious,
                    onNext = onNext,
                    onLike = onLike,
                    modifier = Modifier.padding(bottom = 6.dp),
                    playbackProgress = state.playbackProgress
                )
            }

            // 20. Bottom Navigation Dock
            HomeBottomNav(
                currentTab = state.currentTab,
                onTabSelected = onTabSelected
            )
        }
    }
}
