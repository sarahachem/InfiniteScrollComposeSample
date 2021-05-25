package com.example.neugelb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.neugelb.R
import com.example.neugelb.compose.component.LabelIconCell
import com.example.neugelb.compose.component.LabelValueCell
import com.example.neugelb.compose.component.text.ContentText
import com.example.neugelb.compose.theme.EightDp
import com.example.neugelb.compose.theme.FiftySixDp
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.TwelveDp
import com.example.neugelb.compose.theme.TwentyFourDp
import com.example.neugelb.model.InfoAndCredits
import java.util.Locale

const val DIRECTOR_TITLE: String = "Director"

@ExperimentalMaterialApi
@Composable
fun MovieInfoBottomSheet(
    info: InfoAndCredits?,
    viewModel: MoviesViewModel
) {
    Column {
        info?.let { info ->
            LabelValueCell(
                labelText = stringResource(id = R.string.overview),
                modifier = Modifier.padding(horizontal = TwentyFourDp),
                valueText = info.overview?.takeIf { it.isNotEmpty() }
                    ?: stringResource(id = R.string.no_overview),
            )

            info.tagline?.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    topDivider = true,
                    labelText = stringResource(id = R.string.tagline),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it,
                )
            }

            info.original_language.takeIf { it.isNotEmpty() }?.let { lang ->
                LabelValueCell(
                    topDivider = true,
                    labelText = stringResource(id = R.string.language),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = Locale(lang).displayLanguage,
                )
            }

            info.genres.map { it.name }.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    topDivider = true,
                    labelText = stringResource(id = R.string.genres),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it.joinToString(", "),
                )
            }

            val actors = info.credits.cast.takeIf { it.isNotEmpty() }
            actors?.subList(0, actors.size.coerceAtMost(5))
                ?.let {
                    LabelValueCell(
                        topDivider = true,
                        labelText = stringResource(id = R.string.cast),
                        modifier = Modifier.padding(horizontal = TwentyFourDp),
                        valueText = it.joinToString(", ") { it.name },
                    )
                }

            val directors = info.credits.crew.filter { it.job == DIRECTOR_TITLE }
            directors.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    topDivider = true,
                    labelText = stringResource(id = R.string.directors),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it.joinToString(", ") { it.name },
                )
            }

            LabelValueCell(
                topDivider = true,
                labelText = stringResource(id = R.string.rating),
                modifier = Modifier.padding(horizontal = TwentyFourDp),
                valueText = info.popularity.toString(),
            )

            info.homepage?.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    topDivider = true,
                    labelText = stringResource(id = R.string.homepage),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it
                )
            }
            info.firstYoutubeVideo()?.let {
                LabelIconCell(
                    clickable = true,
                    onClick = {
                        viewModel.playTrailer(it.key)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play",
                            tint = NeugelbTheme.colors.textPlaceholder
                        )
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeugelbTheme.colors.divider)
                        .padding(horizontal = TwelveDp, vertical = EightDp),
                    text = it.name
                )
            }
        } ?: NoDataBox()
    }
}

@Composable
fun NoDataBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(FiftySixDp),
        contentAlignment = Alignment.Center
    ) {
        ContentText(text = stringResource(id = R.string.no_data))
    }
}

private fun InfoAndCredits.firstYoutubeVideo() = videos.results.firstOrNull { it.site == "YouTube" }

