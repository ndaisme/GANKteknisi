package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.AccentSilver
import com.example.ui.theme.White

@Composable
fun NeoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AccentSilver,
    shadowColor: Color = White,
    borderColor: Color = BlackPrimary,
    cornerRadius: Dp = 20.dp,
    strokeWidth: Dp = 4.dp,
    shadowOffset: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, RoundedCornerShape(cornerRadius))
                .border(strokeWidth, borderColor, RoundedCornerShape(cornerRadius))
        )
        
        // Main layer
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(cornerRadius))
                .border(strokeWidth, borderColor, RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            ProvideTextStyle(
                value = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    color = BlackPrimary
                )
            ) {
                content()
            }
        }
    }
}
