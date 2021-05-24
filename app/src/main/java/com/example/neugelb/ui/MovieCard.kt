package com.example.neugelb.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.ThirtySixDp
import com.example.neugelb.compose.theme.TwelveDp

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    title: String,
    url: String?,
    enabled: Boolean = false,
    onMovieClicked: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThirtySixDp, vertical = TwelveDp)
            .clickable(enabled = enabled) { onMovieClicked?.invoke() },
        shape = MaterialTheme.shapes.large,
        elevation = 0.dp,
    ) {
        MovieBackgroundImage(url = url, title = title)
    }
}

@Preview
@Composable
fun MovieCardPreview() {
    NeugelbTheme {
        MovieCard(
            title = "Movie title",
            url = "",
            onMovieClicked = {}
        )
    }
}
