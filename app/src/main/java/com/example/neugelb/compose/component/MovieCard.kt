package com.example.neugelb.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neugelb.compose.component.text.ContentText
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.TwelveDp
import com.example.neugelb.compose.theme.TwentyFourDp
import com.example.neugelb.model.MovieBackgroundImage

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
            .padding(horizontal = TwentyFourDp, vertical = TwelveDp)
            .clickable(enabled = enabled) { onMovieClicked?.invoke() },
        shape = MaterialTheme.shapes.large,
        elevation = 0.dp,
    ) {
        Column(modifier = Modifier.padding(TwentyFourDp)) {
            MovieBackgroundImage(title = title, url = url)
            Spacer(modifier = Modifier.size(TwelveDp))
            ContentText(modifier = Modifier.padding(start = TwentyFourDp), text = title)
        }
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
