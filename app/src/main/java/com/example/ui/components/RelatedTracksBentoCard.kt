package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Recommend
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MusicTrack
import com.example.ui.ReonTokens
import com.example.ui.theme.ReonColors
import com.example.ui.theme.ReonSpacing

@Composable
fun RelatedTracksBentoCard(
    tracks: List<MusicTrack>,
    onTrackSelect: (MusicTrack) -> Unit,
    modifier: Modifier = Modifier
) {
    BentoCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = 0.dp
    ) {
        Column(modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Recommend,
                        contentDescription = null,
                        tint = ReonTokens.InkHigh,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "RELATED ARCHIVES",
                        style = ReonTokens.LabelMono,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        color = ReonTokens.InkLow
                    )
                }
                Text(
                    text = "EXPAND",
                    style = ReonTokens.LabelMono,
                    color = ReonTokens.InkHigh,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tracks, key = { it.id }) { track ->
                RelatedTrackItem(track = track, onClick = { onTrackSelect(track) })
            }
        }
    }
}

@Composable
private fun RelatedTrackItem(
    track: MusicTrack,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(4.dp)
    Row(
        modifier = Modifier
            .width(210.dp)
            .clip(shape)
            .background(Color(0xFFFAFAFA))
            .border(1.dp, ReonTokens.Hairline, shape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrackArtImage(
            url = track.albumArtUrl,
            contentDescription = track.title,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(3.dp))
        )

        Spacer(Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = ReonTokens.BodyMedium,
                fontWeight = FontWeight.Medium,
                color = ReonTokens.InkHigh,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                style = ReonTokens.BodySmall,
                color = ReonTokens.InkMid,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = track.durationLabel,
                style = ReonTokens.DurationText,
                fontSize = 10.sp
            )
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF18181B)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play track",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
