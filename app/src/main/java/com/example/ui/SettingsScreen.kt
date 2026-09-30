package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.reonExtras

/**
 * REON — Settings Screen (Legal & System Information)
 * Pixel-perfect match to user's uploaded reference UI image (Image 2):
 * - Top Header: Back (<), "Settings" title (18sp, Bold), "v2.4.0" version tag
 * - Category Header: "LEGAL & SYSTEM INFORMATION" (left), "LEGAL" (right)
 * - Grouped Card:
 *   1. "Open Source Licenses" -> "Third-party software, codec attributions & audio librari..."
 *   2. "About Project" -> "REON Minimal Audiophile Architecture • Sona Engine ..."
 *   3. "Terms & Conditions" -> "Software license agreement and bit-perfect data tele..."
 * - Footer: "REON • BIT-PERFECT REPRODUCTION", "VERSION 2.4.0 (BUILD 919)"
 */
@Composable
fun SettingsScreen(
    state: HomeState,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onUpdateProfile: (name: String, bio: String) -> Unit = { _, _ -> },
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
    modifier: Modifier = Modifier
) {
    var activeDialog by remember { mutableStateOf<String?>(null) }

    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant
    val cardBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("reon_settings_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 160.dp)
        ) {
            // 1. Top Bar (< Settings | v2.4.0)
            item(key = "settings_top_bar") {
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
                            text = "Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            fontSize = 19.sp
                        )
                    }

                    Text(
                        text = "v2.4.0",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.5.sp,
                        color = onSurfaceMuted,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }

            // 2. Category Header: LEGAL & SYSTEM INFORMATION | LEGAL
            item(key = "settings_category_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = ReonSpacing.margin,
                            end = ReonSpacing.margin,
                            top = 16.dp,
                            bottom = 8.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LEGAL & SYSTEM INFORMATION",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceMuted,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "LEGAL",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.sp,
                        color = onSurfaceMuted,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // 3. Settings Card Container (Open Source Licenses, About Project, Terms & Conditions)
            item(key = "settings_grouped_card") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBackground)
                        .border(ReonSize.hairline, hairlineColor, RoundedCornerShape(14.dp))
                        .padding(vertical = 6.dp)
                ) {
                    Column {
                        // Row 1: Open Source Licenses
                        SettingsRowItem(
                            title = "Open Source Licenses",
                            subtitle = "Third-party software, codec attributions & audio librari...",
                            onClick = { activeDialog = "licenses" }
                        )

                        HorizontalDivider(
                            thickness = ReonSize.hairline,
                            color = hairlineColor,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        // Row 2: About Project
                        SettingsRowItem(
                            title = "About Project",
                            subtitle = "REON Minimal Audiophile Architecture • Sona Engine ...",
                            onClick = { activeDialog = "about" }
                        )

                        HorizontalDivider(
                            thickness = ReonSize.hairline,
                            color = hairlineColor,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        // Row 3: Terms & Conditions
                        SettingsRowItem(
                            title = "Terms & Conditions",
                            subtitle = "Software license agreement and bit-perfect data tele...",
                            onClick = { activeDialog = "terms" }
                        )
                    }
                }
            }

            // 4. Footer Branding & Build info
            item(key = "settings_footer") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReonSpacing.margin, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REON • BIT-PERFECT REPRODUCTION",
                        style = ReonTokens.LabelMono,
                        fontSize = 11.sp,
                        color = onSurfaceMuted,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "VERSION 2.4.0 (BUILD 919)",
                        style = ReonTokens.LabelMono,
                        fontSize = 10.5.sp,
                        color = onSurfaceMuted.copy(alpha = 0.8f),
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Dialogs
        when (activeDialog) {
            "licenses" -> {
                AlertDialog(
                    onDismissRequest = { activeDialog = null },
                    containerColor = cardBackground,
                    shape = RoundedCornerShape(14.dp),
                    title = {
                        Text(
                            text = "Open Source Licenses",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "• Jetpack Compose (Apache 2.0)\n• KotlinX Coroutines & Serialization (Apache 2.0)\n• Coil Image Engine (Apache 2.0)\n• Material 3 Components (Apache 2.0)\n• LibFLAC & DSD Audio Decoders (BSD-3-Clause)",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            lineHeight = 20.sp
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { activeDialog = null }) {
                            Text("Close", color = onSurface, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
            "about" -> {
                AlertDialog(
                    onDismissRequest = { activeDialog = null },
                    containerColor = cardBackground,
                    shape = RoundedCornerShape(14.dp),
                    title = {
                        Text(
                            text = "About REON",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "REON Minimal Audiophile Architecture with bit-perfect hardware bypass, Sona Audio Engine, Native 24-bit/192kHz PCM, and 1-bit DSD stream reproduction.",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            lineHeight = 20.sp
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { activeDialog = null }) {
                            Text("OK", color = onSurface, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
            "terms" -> {
                AlertDialog(
                    onDismissRequest = { activeDialog = null },
                    containerColor = cardBackground,
                    shape = RoundedCornerShape(14.dp),
                    title = {
                        Text(
                            text = "Terms & Conditions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "Software license agreement and bit-perfect data telemetry protocols. All high-resolution acoustic assets are provided under exclusive studio licensing.",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceMuted,
                            lineHeight = 20.sp
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { activeDialog = null }) {
                            Text("Accept", color = onSurface, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsRowItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceMuted = MaterialTheme.reonExtras.onSurfaceMuted

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = onSurface,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = onSurfaceMuted,
                fontSize = 12.5.sp,
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = onSurfaceMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
