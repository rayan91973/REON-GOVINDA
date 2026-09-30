package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.reonExtras

/**
 * REON — 3 Navigation Tabs: Home, Search, Library
 * (Profile removed from bottom dock)
 */
@Composable
fun HomeBottomNav(
    currentTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackground = MaterialTheme.colorScheme.surfaceContainerLowest
    val hairlineColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(navBackground)
            .navigationBarsPadding()
            .testTag("reon_bottom_nav")
    ) {
        HorizontalDivider(
            thickness = ReonSize.hairline,
            color = hairlineColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ReonSize.bottomNavHeight)
                .background(navBackground),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTabItem(
                label = "Home",
                icon = Icons.Rounded.GridView,
                isSelected = currentTab == HomeTab.Home,
                onClick = { onTabSelected(HomeTab.Home) },
                testTag = "nav_tab_home",
                modifier = Modifier.weight(1f)
            )

            NavTabItem(
                label = "Search",
                icon = Icons.Rounded.Search,
                isSelected = currentTab == HomeTab.Search,
                onClick = { onTabSelected(HomeTab.Search) },
                testTag = "nav_tab_search",
                modifier = Modifier.weight(1f)
            )

            NavTabItem(
                label = "Library",
                icon = Icons.AutoMirrored.Rounded.QueueMusic,
                isSelected = currentTab == HomeTab.Library,
                onClick = { onTabSelected(HomeTab.Library) },
                testTag = "nav_tab_library",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val activeColor = MaterialTheme.colorScheme.onSurface
    val mutedColor = MaterialTheme.reonExtras.onSurfaceMuted

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else mutedColor,
        animationSpec = tween(150),
        label = "nav_color"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = activeColor.copy(alpha = 0.08f)),
                onClick = onClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}
