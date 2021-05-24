package com.example.neugelb.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.neugelb.compose.theme.NeugelbTheme

@Composable
fun ClickableCircleProgress(modifier: Modifier, clickable: Boolean) {
    Box(
        modifier = modifier
            .clickable { clickable },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = NeugelbTheme.colors.mainColor)
    }
}