package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GlassBlue
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextPrimary

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    borderColor: Color = BorderSubtle,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    testTag: String? = null,
    content: @Composable () -> Unit
) {
    val clickableModifier = if (onClick != null) {
        modifier
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .clip(shape)
            .clickable(onClick = onClick)
    } else {
        modifier.then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
    }

    Surface(
        modifier = clickableModifier
            .border(
                BorderStroke(
                    borderWidth,
                    Brush.verticalGradient(
                        listOf(borderColor, borderColor.copy(alpha = 0.1f))
                    )
                ),
                shape
            ),
        shape = shape,
        color = CardNavy.copy(alpha = 0.85f),
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            CardNavyElevated.copy(alpha = 0.6f),
                            CardNavy.copy(alpha = 0.9f)
                        )
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
fun GlowingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isSecondary: Boolean = false,
    isDanger: Boolean = false,
    testTag: String = "glowing_button"
) {
    val gradientBrush = when {
        isDanger -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFDC2626), Color(0xFFEF4444))
        )
        isSecondary -> Brush.horizontalGradient(
            colors = listOf(CardNavyElevated, CardNavy)
        )
        else -> Brush.horizontalGradient(
            colors = listOf(FireBlueDark, FireBluePrimary, SkyBlueAccent)
        )
    }

    val contentColor = when {
        isSecondary -> FireBluePrimary
        else -> DeepNavyBg
    }

    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .testTag(testTag)
            .defaultMinSize(minHeight = 48.dp)
            .clip(shape)
            .background(if (enabled) gradientBrush else Brush.linearGradient(listOf(Color.DarkGray, Color.Gray)))
            .then(
                if (isSecondary) Modifier.border(1.dp, BorderGlow, shape)
                else Modifier
            )
            .clickable(
                enabled = enabled && !isLoading,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = if (isSecondary) FireBluePrimary else DeepNavyBg,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = contentColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
