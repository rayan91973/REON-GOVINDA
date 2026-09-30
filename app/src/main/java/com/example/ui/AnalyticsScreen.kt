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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.SurroundSound
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.HorizontalDivider
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

/**
 * REON — Analytics Screen (Listening Insights / Telemetry Bay)
 * Pixel-perfect match to user's uploaded reference UI image (Image 1):
 * - Top Header: "REON" brand wordmark, Tune filter, circular Profile Avatar
 * - Sub-header: Back (<), "TELEMETRY BAY", "Listening Insights", Period switcher (Week, Month, Year)
 * - Card 1: Acoustic Exposure (Real-Time) - 48h 12m +14%, Bit-depth segmented bar, Avg Session & DAC Passthrough tiles
 * - Card 2: Acoustic Immersion (Daily Rhythm) - Peak Session Sat • 9.4 hrs bar telemetry + callout
 * - Card 3: Telemetry Spectrum (Resolution Analysis) - PCM Studio Master 62%, DSD 26%, Redbook 12%, DR14 Dynamic profile
 * - Card 4: Acoustic Residency (Top Artists & Ensembles) - Solaris & Kaelen, Aura Sound Lab, Mirage Architecture, Kaelen Solo
 * - Card 5: Spatial Classification (Acoustic Mood Spectrum) - Ambient 45%, Modular 35%, Brutalist 20%
 * - Bottom Action: Export Telemetry Log button + Share button
 */
@Composable
fun AnalyticsScreen(
    state: HomeState,
    onTrackSelect: (TrackItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onLike: () -> Unit = {},
    onOpenNowPlaying: () -> Unit = {},
    onShowToast: (String) -> Unit = {}
) {
    var selectedPeriod by remember { mutableStateOf("Week") }
    val periods = listOf("Week", "Month", "Year")

    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_analytics_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 160.dp)
        ) {
            // 1. Top Bar (< REON | Tune, Avatar >)
            item(key = "analytics_top_bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REON",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = onSurface,
                        letterSpacing = 1.5.sp,
                        fontSize = 20.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onShowToast("Telemetry settings") },
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

            // 2. Sub-header Navigation Row (< Back | TELEMETRY BAY / Listening Insights | Week, Month, Year)
            item(key = "analytics_sub_header") {
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

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "TELEMETRY BAY",
                                style = ReonTokens.LabelMono,
                                fontSize = 9.5.sp,
                                color = onSurfaceMuted,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(1.dp))
                            Text(
                                text = "Listening Insights",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 17.sp
                            )
                        }
                    }

                    // Period Switcher (Week, Month, Year)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                            .padding(2.dp)
                    ) {
                        Row {
                            periods.forEach { period ->
                                val isSelected = period == selectedPeriod
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) primaryColor else Color.Transparent)
                                        .clickable { selectedPeriod = period }
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = period,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) onPrimaryColor else onSurfaceMuted,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Card 1: Acoustic Exposure (Real-Time)
            item(key = "card_acoustic_exposure") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ACOUSTIC EXPOSURE",
                                    style = ReonTokens.LabelMono,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = onSurfaceMuted,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(surfaceHigh)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "REAL-TIME",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 9.sp,
                                        color = onSurfaceMuted,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Soundwave icon container
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.GraphicEq,
                                    contentDescription = null,
                                    tint = onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Headline 48h 12m +14%
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "48h 12m",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                fontSize = 32.sp
                            )

                            Box(
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(surfaceHigh)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.TrendingUp,
                                        contentDescription = null,
                                        tint = onSurface,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(Modifier.width(3.dp))
                                    Text(
                                        text = "+14%",
                                        style = ReonTokens.LabelMono,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = onSurface
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        // Subtitle: 94% Master Bit-Perfect | DSD • FLAC 24-BIT
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "94% Master Bit-Perfect",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "DSD • FLAC 24-BIT",
                                style = ReonTokens.LabelMono,
                                fontSize = 10.sp,
                                color = onSurfaceMuted
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        // Segmented bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(surfaceHigh)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Box(modifier = Modifier.weight(0.68f).fillMaxHeight().background(primaryColor))
                                Box(modifier = Modifier.weight(0.26f).fillMaxHeight().background(Color(0xFF64748B)))
                                Box(modifier = Modifier.weight(0.06f).fillMaxHeight().background(Color(0xFFCBD5E1)))
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(primaryColor))
                                Spacer(Modifier.width(4.dp))
                                Text("FLAC 24-bit 68%", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF64748B)))
                                Spacer(Modifier.width(4.dp))
                                Text("DSD 26%", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                Spacer(Modifier.width(4.dp))
                                Text("16-bit 6%", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // 2-Column Tiles: AVG SESSION & DAC PASSTHROUGH
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Tile 1
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text("AVG SESSION", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text("1h 24m", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 16.sp)
                                        Text(" / day", style = MaterialTheme.typography.bodySmall, color = onSurfaceMuted, fontSize = 11.sp, modifier = Modifier.padding(bottom = 1.dp))
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(onSurface))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Deep Focus State", style = MaterialTheme.typography.bodySmall, color = onSurfaceMuted, fontSize = 11.sp)
                                    }
                                }
                            }

                            // Tile 2
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(surfaceHigh)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text("DAC PASSTHROUGH", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text("100%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 16.sp)
                                        Text(" B-PERF", style = ReonTokens.LabelMono, color = onSurfaceMuted, fontSize = 10.sp, modifier = Modifier.padding(bottom = 2.dp, start = 2.dp))
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null, tint = onSurface, modifier = Modifier.size(11.dp))
                                        Spacer(Modifier.width(3.dp))
                                        Text("Hardware Locked", style = MaterialTheme.typography.bodySmall, color = onSurfaceMuted, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Card 2: Acoustic Immersion (Daily Rhythm)
            item(key = "card_daily_rhythm") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ACOUSTIC IMMERSION", style = ReonTokens.LabelMono, fontSize = 11.sp, color = onSurfaceMuted, letterSpacing = 0.5.sp)
                            Text("PEAK SESSION", style = ReonTokens.LabelMono, fontSize = 10.sp, color = onSurfaceMuted)
                        }

                        Spacer(Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Daily Rhythm", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 18.sp)
                            Text("Sat • 9.4 hrs", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 13.5.sp)
                        }

                        Spacer(Modifier.height(16.dp))

                        // 7-day Bar Telemetry Chart (M, T, W, T, F, S, S)
                        val days = listOf("M" to 3.2f, "T" to 4.5f, "W" to 5.0f, "T" to 4.0f, "F" to 6.8f, "S" to 9.4f, "S" to 4.8f)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            days.forEach { (day, hours) ->
                                val isPeak = day == "S" && hours == 9.4f
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    if (isPeak) {
                                        Text(
                                            text = "9.4h",
                                            style = ReonTokens.LabelMono,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = onSurface
                                        )
                                        Spacer(Modifier.height(3.dp))
                                    }

                                    Box(
                                        modifier = Modifier
                                            .width(22.dp)
                                            .height(((hours / 9.4f) * 60).dp)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(if (isPeak) primaryColor else surfaceHigh)
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = day,
                                        style = ReonTokens.LabelMono,
                                        fontSize = 11.sp,
                                        fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isPeak) onSurface else onSurfaceMuted
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Callout Box inside card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(8.dp))
                                .padding(10.dp)
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
                                    Icon(
                                        imageVector = Icons.Rounded.SurroundSound,
                                        contentDescription = null,
                                        tint = onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "Peak session coincided with Binaural Ambient session",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = onSurface,
                                        fontSize = 11.5.sp,
                                        lineHeight = 15.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("SAT", style = ReonTokens.LabelMono, fontSize = 9.sp, color = onSurfaceMuted)
                                    Text("23:40", style = ReonTokens.LabelMono, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = onSurface)
                                }
                            }
                        }
                    }
                }
            }

            // 5. Card 3: Telemetry Spectrum (Resolution Analysis)
            item(key = "card_resolution_analysis") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TELEMETRY SPECTRUM", style = ReonTokens.LabelMono, fontSize = 11.sp, color = onSurfaceMuted, letterSpacing = 0.5.sp)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(surfaceHigh)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Rounded.BarChart, contentDescription = null, tint = onSurface, modifier = Modifier.size(11.dp))
                                    Spacer(Modifier.width(3.dp))
                                    Text("DR14 Master", style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurface, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        Text("Resolution Analysis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 18.sp)

                        Spacer(Modifier.height(14.dp))

                        // Row 1: PCM Studio Master (62%)
                        ResolutionFormatRow(
                            title = "PCM Studio Master",
                            badge = "96-192kHz",
                            subtitle = "24-bit Precision Stream",
                            percentage = "62%",
                            tracks = "142 tracks",
                            iconType = "pcm"
                        )

                        Spacer(Modifier.height(8.dp))

                        // Row 2: DSD Direct Stream (26%)
                        ResolutionFormatRow(
                            title = "DSD Direct Stream",
                            badge = "DSD64/256",
                            subtitle = "1-bit • 2.822 - 11.2MHz",
                            percentage = "26%",
                            tracks = "58 tracks",
                            iconType = "dsd"
                        )

                        Spacer(Modifier.height(8.dp))

                        // Row 3: Redbook FLAC (12%)
                        ResolutionFormatRow(
                            title = "Redbook FLAC",
                            badge = "16/44.1",
                            subtitle = "CD Lossless Standard",
                            percentage = "12%",
                            tracks = "28 tracks",
                            iconType = "flac"
                        )

                        Spacer(Modifier.height(14.dp))

                        // Dynamic Range Index Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceHigh)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("DYNAMIC RANGE INDEX", style = ReonTokens.LabelMono, fontSize = 9.sp, color = onSurfaceMuted)
                                    Spacer(Modifier.height(2.dp))
                                    Text("DR14 Studio Master Dynamic Profile", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = onSurface, fontSize = 12.sp)
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(cardBackground)
                                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("UNCOMPRESSED", style = ReonTokens.LabelMono, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = onSurface)
                                }
                            }
                        }
                    }
                }
            }

            // 6. Card 4: Acoustic Residency (Top Artists & Ensembles)
            item(key = "card_top_artists") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ACOUSTIC RESIDENCY", style = ReonTokens.LabelMono, fontSize = 11.sp, color = onSurfaceMuted, letterSpacing = 0.5.sp)
                            Text("ALL (18)", style = ReonTokens.LabelMono, fontSize = 10.sp, color = onSurfaceMuted)
                        }

                        Spacer(Modifier.height(4.dp))

                        Text("Top Artists & Ensembles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 18.sp)

                        Spacer(Modifier.height(12.dp))

                        val artists = listOf(
                            Triple("Solaris & Kaelen", "18.4 hrs streamed • 24-BIT MASTER", 1),
                            Triple("Aura Sound Lab", "12.1 hrs streamed • DSD 5.6M", 2),
                            Triple("Mirage Architecture", "8.6 hrs streamed • 24/192 FLAC", 3),
                            Triple("Kaelen Solo Archive", "5.2 hrs streamed • WAV 32-BIT", 4)
                        )

                        artists.forEachIndexed { index, (name, meta, seed) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onShowToast("Playing $name") }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = String.format("%02d", index + 1),
                                    style = ReonTokens.LabelMono,
                                    fontSize = 11.5.sp,
                                    color = onSurfaceMuted,
                                    modifier = Modifier.width(26.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(surfaceHigh)
                                ) {
                                    TrackArtImage(
                                        url = getArtUrlForSeed(seed),
                                        contentDescription = name,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 14.sp)
                                    Spacer(Modifier.height(1.dp))
                                    Text(meta, style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                                }

                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = "Play",
                                    tint = onSurfaceMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 7. Card 5: Spatial Classification (Acoustic Mood Spectrum)
            item(key = "card_mood_spectrum") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("SPATIAL CLASSIFICATION", style = ReonTokens.LabelMono, fontSize = 11.sp, color = onSurfaceMuted, letterSpacing = 0.5.sp)
                            Text("3 SIGNATURES", style = ReonTokens.LabelMono, fontSize = 10.sp, color = onSurfaceMuted)
                        }

                        Spacer(Modifier.height(4.dp))

                        Text("Acoustic Mood Spectrum", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 18.sp)

                        Spacer(Modifier.height(14.dp))

                        // Spectrum 1: Ambient & Deep Spatial (45%)
                        MoodSpectrumRow(
                            title = "Ambient & Deep Spatial",
                            percent = 45,
                            desc = "21h 40m • Reverberant field density 0.82",
                            barColor = primaryColor
                        )

                        Spacer(Modifier.height(12.dp))

                        // Spectrum 2: Progressive Modular Electronica (35%)
                        MoodSpectrumRow(
                            title = "Progressive Modular Electronica",
                            percent = 35,
                            desc = "16h 52m • Fast transient response profile",
                            barColor = Color(0xFF64748B)
                        )

                        Spacer(Modifier.height(12.dp))

                        // Spectrum 3: Brutalist Minimalist Acoustics (20%)
                        MoodSpectrumRow(
                            title = "Brutalist Minimalist Acoustics",
                            percent = 20,
                            desc = "9h 40m • Direct sound field, dry mastered",
                            barColor = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // 8. Bottom Action Buttons: Export Telemetry Log & Share
            item(key = "analytics_export_actions") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Export Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .clickable { onShowToast("Exported Telemetry Log") },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = null,
                                tint = onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Export Telemetry Log",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = onSurface,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Share Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceHigh)
                            .clickable { onShowToast("Shared Listening Insights") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share",
                            tint = onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResolutionFormatRow(
    title: String,
    badge: String,
    subtitle: String,
    percentage: String,
    tracks: String,
    iconType: String
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(surfaceHigh)
            .padding(10.dp)
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
                        .size(34.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF18181B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.GraphicEq,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 13.5.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color.White.copy(alpha = 0.8f))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(badge, style = ReonTokens.LabelMono, fontSize = 8.5.sp, color = Color(0xFF18181B), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(1.dp))
                    Text(subtitle, style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(percentage, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 17.sp)
                Text(tracks, style = ReonTokens.LabelMono, fontSize = 9.sp, color = onSurfaceMuted)
            }
        }
    }
}

@Composable
private fun MoodSpectrumRow(
    title: String,
    percent: Int,
    desc: String,
    barColor: Color
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(barColor))
                Spacer(Modifier.width(6.dp))
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = onSurface, fontSize = 13.5.sp)
            }

            Text("$percent%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = onSurface, fontSize = 14.sp)
        }

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(surfaceHigh)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(desc, style = ReonTokens.LabelMono, fontSize = 9.5.sp, color = onSurfaceMuted)
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
