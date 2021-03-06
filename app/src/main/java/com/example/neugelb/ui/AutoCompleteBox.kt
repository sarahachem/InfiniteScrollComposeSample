package com.example.neugelb.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.model.AutoCompleteItem

@ExperimentalAnimationApi
@Composable
fun <T : AutoCompleteItem> AutoCompleteBox(
    isSearching: Boolean,
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    content: @Composable () -> Unit,
    onSelectItem: (T) -> Unit
) {
    Column(
        modifier = Modifier.background(NeugelbTheme.colors.divider),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
        AnimatedVisibility(visible = isSearching) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items.forEach { item ->
                    Box(modifier = Modifier.clickable { onSelectItem(item) }) {
                        itemContent(item)
                    }
                }
            }
        }
    }
}
