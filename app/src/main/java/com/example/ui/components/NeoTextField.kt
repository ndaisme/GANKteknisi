package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.GrayText
import com.example.ui.theme.Silver
import com.example.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    shadowColor: Color = Silver,
    borderColor: Color = BlackPrimary,
    cornerRadius: Dp = 12.dp,
    strokeWidth: Dp = 3.dp,
    shadowOffset: Dp = 4.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, RoundedCornerShape(cornerRadius))
                .border(strokeWidth, borderColor, RoundedCornerShape(cornerRadius))
        )
        
        // Main Input
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontWeight = FontWeight.Bold) },
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(cornerRadius))
                .border(strokeWidth, borderColor, RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius)),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = BlackPrimary,
                unfocusedLabelColor = GrayText,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                color = BlackPrimary
            )
        )
    }
}
