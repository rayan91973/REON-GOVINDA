package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.ReonTokens
import com.example.ui.theme.ReonColors
import com.example.ui.theme.ReonSpacing

/**
 * Sona Architectural Content Card
 * Flat white base, 8dp (rounded-lg) radius, hairline 1px border, soft ambient float.
 */
@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    background: Color = ReonColors.Surface,
    contentPadding: Dp = ReonSpacing.lg,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(ReonSpacing.cardRadius) // 8.dp
    Column(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = shape,
                ambientColor = Color(0x0A000000),
                spotColor = Color(0x0C18181B)
            )
            .clip(shape)
            .background(background)
            .border(1.dp, ReonTokens.Hairline, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        indication = ripple(bounded = true, color = Color(0x18181B).copy(alpha = 0.06f)),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(contentPadding),
        content = content,
    )
}
