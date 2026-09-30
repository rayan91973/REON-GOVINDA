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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.SaveAlt
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TrackArtImage
import com.example.ui.theme.reonExtras

data class NotificationItemModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val isUnread: Boolean = false,
    val badge: String = "",
    val tags: List<String> = emptyList(),
    val actionType: String = "none", // "play", "download", "tune_in", "pre_save"
    val actionText: String = "",
    val artSeed: Int = 1,
    val isSystemIcon: Boolean = false,
    val iconType: String = ""
)

data class NotificationGroupModel(
    val groupTitle: String,
    val rightTag: String,
    val items: List<NotificationItemModel>
)

/**
 * REON — Notifications Screen
 * Pixel-perfect match to user's uploaded reference UI image (Image 2):
 * - Header: Back (<), "Notifications", DoneAll (✓✓), Settings gear
 * - Signal Feed Status: "● SIGNAL FEED: 192kHz BIT-DIRECT ACTIVE", "6 EVENTS"
 * - Filter Pills: "All 6" (solid black), "Releases 3", "Lossless Drops 2", "System 1"
 * - Queue Priority: "QUEUE PRIORITY: RECENCY", "✓ Mark all read"
 * - Groups:
 *   - "TODAY" (2 UNREAD): Solaris & Kaelen master release (Play Master), Lossless Vault Drop (Download)
 *   - "YESTERDAY" (ARCHIVED): Aura Sound Lab is Live (Tune In), Firmware & DAC Engine Sync (v3.4.1 PATCH)
 *   - "EARLIER THIS WEEK" (VERIFIED): New Album Pre-save (Pre-save), Storage Allocation Synced (4.2 GB NVMe)
 * - Telemetry & Signal Settings Card: "Configure bit-depth threshold notifications..." + "Manage >"
 */
@Composable
fun NotificationScreen(
    state: HomeState,
    onBackClick: () -> Unit = {},
    onTrackSelect: (TrackItem) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenDownloads: () -> Unit = {},
    onMarkAsRead: (String) -> Unit = {},
    onMarkAllAsRead: () -> Unit = {},
    onClearAll: () -> Unit = {},
    onDeleteNotification: (String) -> Unit = {},
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

    var selectedFilter by remember { mutableStateOf("All 6") }
    val filters = listOf("All 6", "Releases 3", "Lossless Drops 2", "System 1")

    val groups = remember { sampleNotificationGroups() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_notification_screen")
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp)
        ) {
            // 1. Top Bar (< Notifications | DoneAll, Settings >)
            item(key = "notif_top_bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 19.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onMarkAllAsRead()
                                onShowToast("All notifications marked as read")
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DoneAll,
                                contentDescription = "Mark all read",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onOpenSettings,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "Settings",
                                tint = onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // 2. Signal Feed Status Banner (● SIGNAL FEED: 192kHz BIT-DIRECT ACTIVE | 6 EVENTS)
            item(key = "notif_signal_banner") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceHigh)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(onSurface)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "SIGNAL FEED: 192kHz BIT-DIRECT ACTIVE",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Text(
                            text = "6 EVENTS",
                            style = ReonTokens.LabelMono,
                            fontSize = 10.sp,
                            color = onSurfaceMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 3. Filter Pills (All 6, Releases 3, Lossless Drops 2, System 1)
            item(key = "notif_filter_chips") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
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

            // 4. Queue Priority: Recency | ✓ Mark all read
            item(key = "notif_priority_bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QUEUE PRIORITY: RECENCY",
                        style = ReonTokens.LabelMono,
                        fontSize = 10.5.sp,
                        color = onSurfaceMuted,
                        letterSpacing = 0.5.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onMarkAllAsRead()
                            onShowToast("Marked all read")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = onSurface,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Mark all read",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = onSurface,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // 5. Timeline Notification Items
            groups.forEach { group ->
                item(key = "notif_group_${group.groupTitle}") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = ReonSpacing.margin,
                                end = ReonSpacing.margin,
                                top = 12.dp,
                                bottom = 4.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.groupTitle,
                            style = ReonTokens.LabelMono,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = group.rightTag,
                            style = ReonTokens.LabelMono,
                            fontSize = 10.5.sp,
                            color = onSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                items(group.items, key = { it.id }) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ReonSpacing.margin, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(cardBackground)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Thumbnail / Icon Container
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(surfaceHigh)
                                ) {
                                    if (item.isSystemIcon) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (item.iconType == "dsp") Icons.Rounded.Memory else Icons.Rounded.SaveAlt,
                                                contentDescription = null,
                                                tint = onSurface,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    } else {
                                        TrackArtImage(
                                            url = getArtUrlForSeed(item.artSeed),
                                            contentDescription = item.title,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    // Badge in artwork
                                    if (item.badge.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .background(Color(0xD9000000))
                                                .padding(vertical = 1.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = item.badge,
                                                style = ReonTokens.LabelMono,
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (item.badge == "LIVE") Color.Red else Color.White
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                // Title, Subtitle, Time & Unread Indicator
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = onSurface,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = item.timeAgo,
                                                style = ReonTokens.LabelMono,
                                                fontSize = 10.5.sp,
                                                color = onSurfaceMuted
                                            )
                                            if (item.isUnread) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(onSurface)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(3.dp))

                                    Text(
                                        text = item.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = onSurfaceMuted,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            // Tags & Action Button Row
                            if (item.tags.isNotEmpty() || item.actionType != "none") {
                                Spacer(Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Tags on Left
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        item.tags.forEach { tag ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(surfaceHigh)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = tag,
                                                    style = ReonTokens.LabelMono,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = onSurfaceMuted
                                                )
                                            }
                                        }
                                    }

                                    // Action Button on Right
                                    when (item.actionType) {
                                        "play" -> {
                                            Box(
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(primaryColor)
                                                    .clickable { onTrackSelect(TrackItem("nt_sp", item.title, "Solaris & Kaelen", "Master", "4:18", artSeed = 1)) }
                                                    .padding(horizontal = 10.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = onPrimaryColor, modifier = Modifier.size(13.dp))
                                                    Spacer(Modifier.width(4.dp))
                                                    Text(text = item.actionText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = onPrimaryColor, fontSize = 11.sp)
                                                }
                                            }
                                        }
                                        "download" -> {
                                            Box(
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(surfaceHigh)
                                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp))
                                                    .clickable { onOpenDownloads() },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Rounded.Download, contentDescription = "Download", tint = onSurface, modifier = Modifier.size(15.dp))
                                            }
                                        }
                                        "tune_in" -> {
                                            Box(
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(surfaceHigh)
                                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp))
                                                    .clickable { onShowToast("Tuning in to Live Stream") }
                                                    .padding(horizontal = 10.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Rounded.Radio, contentDescription = null, tint = onSurface, modifier = Modifier.size(13.dp))
                                                    Spacer(Modifier.width(4.dp))
                                                    Text(text = item.actionText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 11.sp)
                                                }
                                            }
                                        }
                                        "pre_save" -> {
                                            Box(
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(surfaceHigh)
                                                    .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(6.dp))
                                                    .clickable { onShowToast("Pre-saved Monolith Variations Pt. III") }
                                                    .padding(horizontal = 10.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Rounded.BookmarkBorder, contentDescription = null, tint = onSurface, modifier = Modifier.size(13.dp))
                                                    Spacer(Modifier.width(4.dp))
                                                    Text(text = item.actionText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6. Telemetry & Signal Settings Card
            item(key = "notif_settings_card") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceHigh)
                        .padding(12.dp)
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
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(cardBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Tune,
                                    contentDescription = null,
                                    tint = onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Telemetry & Signal Setti...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    fontSize = 13.5.sp
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "Configure bit-depth threshold notifications, studio transmissions, and master drop alerts.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = onSurfaceMuted,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        Spacer(Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(cardBackground)
                                .clickable { onOpenSettings() }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Manage",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface,
                                    fontSize = 11.5.sp
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = onSurface,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun sampleNotificationGroups(): List<NotificationGroupModel> {
    return listOf(
        NotificationGroupModel(
            groupTitle = "TODAY",
            rightTag = "2 UNREAD",
            items = listOf(
                NotificationItemModel(
                    id = "notif_1",
                    title = "Solaris & Kaelen released a mast...",
                    subtitle = "Album \"Sub-Zero Resonance\" is now streaming in Bit-Perfect 24-Bit / 192kHz FLAC.",
                    timeAgo = "12m",
                    isUnread = true,
                    badge = "FLAC 24",
                    tags = listOf("192kHz", "PCM"),
                    actionType = "play",
                    actionText = "Play Master",
                    artSeed = 1
                ),
                NotificationItemModel(
                    id = "notif_2",
                    title = "Lossless Vault Drop: DSD 11.2MHz",
                    subtitle = "Curated compilation \"Architectural Lows: Volume II\" has been synced to your offline...",
                    timeAgo = "2h",
                    isUnread = true,
                    badge = "DSD 256",
                    tags = listOf("DSD 11.2MHz", "1.8 GB"),
                    actionType = "download",
                    artSeed = 2
                )
            )
        ),
        NotificationGroupModel(
            groupTitle = "YESTERDAY",
            rightTag = "ARCHIVED",
            items = listOf(
                NotificationItemModel(
                    id = "notif_3",
                    title = "Aura Sound Lab is Live",
                    subtitle = "Studio transmission streaming bit-direct from Stockholm at 96kHz / 24-Bit.",
                    timeAgo = "1d",
                    badge = "LIVE",
                    tags = listOf("REON CORE LIVE STREAM"),
                    actionType = "tune_in",
                    actionText = "Tune In",
                    artSeed = 3
                ),
                NotificationItemModel(
                    id = "notif_4",
                    title = "Firmware & DAC Engine Sync",
                    subtitle = "REON CoreAudio Bit-Perfect Engine upgraded to v3.4.1. Ultra-low jitter passthrough active.",
                    timeAgo = "1d",
                    badge = "DSP",
                    tags = listOf("BIT-DIRECT", "v3.4.1 PATCH"),
                    isSystemIcon = true,
                    iconType = "dsp"
                )
            )
        ),
        NotificationGroupModel(
            groupTitle = "EARLIER THIS WEEK",
            rightTag = "VERIFIED",
            items = listOf(
                NotificationItemModel(
                    id = "notif_5",
                    title = "New Album Pre-save Available",
                    subtitle = "Mirage Architecture — \"Monolith Variations Pt. III\" releases Oct 28 in Master Quality.",
                    timeAgo = "3d",
                    badge = "OCT 28",
                    tags = listOf("PRE-ALLOCATE CACHE"),
                    actionType = "pre_save",
                    actionText = "Pre-save",
                    artSeed = 4
                ),
                NotificationItemModel(
                    id = "notif_6",
                    title = "Storage Allocation Synced",
                    subtitle = "14 uncompressed DSD files cached locally (4.2 GB). Bit-perfect offline vault primed.",
                    timeAgo = "5d",
                    badge = "CACHE",
                    tags = listOf("4.2 GB / 128 GB", "INTERNAL NVME"),
                    isSystemIcon = true,
                    iconType = "cache"
                )
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
