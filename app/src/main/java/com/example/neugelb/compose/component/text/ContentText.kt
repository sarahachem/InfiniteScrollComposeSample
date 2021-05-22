package com.example.neugelb.compose.component.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.neugelb.compose.theme.NeugelbTheme

@Composable
fun ContentText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = NeugelbTheme.colors.textPrimary,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    modifier = modifier,
    textAlign = textAlign,
    text = text,
    color = color,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
    style = NeugelbTheme.types.body1
)

@Composable
fun BodyText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign?= null,
    text: String,
    color: Color = NeugelbTheme.colors.textSecondary
) = ContentText(modifier, textAlign = textAlign, text = text, color = color)