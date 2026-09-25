package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MapType
import com.example.data.model.MatchMode
import com.example.data.model.TournamentStatus
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToxicGreen

@Composable
fun ModeBadge(mode: MatchMode) {
    val (bgColor, textColor, label) = when (mode) {
        MatchMode.SOLO -> Triple(Color(0x3300E5FF), FireBluePrimary, "SOLO")
        MatchMode.DUO -> Triple(Color(0x3338BDF8), SkyBlueAccent, "DUO")
        MatchMode.SQUAD -> Triple(Color(0x33FF6D00), FlameOrange, "SQUAD")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(0.5.dp, textColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MapBadge(mapType: MapType) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CardNavyElevated)
            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "📍 ${mapType.displayName}",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusBadge(status: TournamentStatus) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    when (status) {
        TournamentStatus.LIVE -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DangerRed.copy(alpha = 0.2f))
                    .border(0.5.dp, DangerRed, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(DangerRed.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "LIVE NOW",
                    color = DangerRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        TournamentStatus.REGISTRATION_OPEN -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ToxicGreen.copy(alpha = 0.15f))
                    .border(0.5.dp, ToxicGreen, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(ToxicGreen)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "OPEN",
                    color = ToxicGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0x3364748B))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = status.label,
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SlotProgressBar(
    filled: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progress = (filled.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    val isAlmostFull = (total - filled) in 1..5

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Slots Filled",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$filled / $total",
                color = if (isAlmostFull) FlameOrange else FireBluePrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(CardNavyElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress)
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                FireBlueDark,
                                if (isAlmostFull) FlameOrange else FireBluePrimary
                            )
                        )
                    )
            )
        }
    }
}
