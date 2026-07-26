package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.Silver
import com.example.ui.theme.White

@Composable
fun NeoCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    shadowColor: Color = Silver,
    borderColor: Color = BlackPrimary,
    cornerRadius: Dp = 20.dp,
    strokeWidth: Dp = 4.dp,
    shadowOffset: Dp = 6.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, RoundedCornerShape(cornerRadius))
                .border(strokeWidth, borderColor, RoundedCornerShape(cornerRadius))
        )
        
        // Main layer
        val mainModifier = Modifier
            .matchParentSize()
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .border(strokeWidth, borderColor, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            
        Box(
            modifier = if (onClick != null) mainModifier.clickable { onClick() } else mainModifier
        )
        
        // Content wrapper (keeps constraints correct)
        Box(
            modifier = Modifier.clip(RoundedCornerShape(cornerRadius))
        ) {
            content()
        }
    }
}
