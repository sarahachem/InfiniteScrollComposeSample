package com.example.neugelb.compose.component.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.neugelb.compose.theme.NeugelbTheme

@Composable
fun SecondaryText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = NeugelbTheme.colors.textSecondary,
    textAlign: TextAlign
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textAlign = textAlign,
        style = NeugelbTheme.types.secondary
    )
}