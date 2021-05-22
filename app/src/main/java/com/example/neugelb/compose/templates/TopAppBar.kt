package com.example.neugelb.compose.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicTopAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    title: String = "",
    items: List<MoreMenuItem> = emptyList(),
    onOptionClick: (() -> Unit)? = null,
    onBackClick: () -> Unit
) {
    var moreMenuExpanded: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        TopAppBar(
            elevation = 0.dp,
            backgroundColor = backgroundColor,
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    lineHeight = 22.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        tint = MaterialTheme.colors.primary,
                        contentDescription = "Back button"
                    )
                }
            },
            actions = {
                onOptionClick?.let {
                    IconButton(onClick = {
                        it()
                        moreMenuExpanded = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            tint = MaterialTheme.colors.onSurface,
                            contentDescription = null
                        )
                    }
                    if (items.isNotEmpty()) {
                        MoreMenu(
                            items = items,
                            expanded = moreMenuExpanded,
                            onDismissRequest = { moreMenuExpanded = false },
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun MoreMenu(
    items: List<MoreMenuItem>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach {
            DropdownMenuItem(onClick = {
                onDismissRequest()
                it.onClick
            }) {
                Text(stringResource(id = it.stringResourceId))
            }
        }
    }
}

data class MoreMenuItem(val stringResourceId: Int, val onClick: () -> Unit)
