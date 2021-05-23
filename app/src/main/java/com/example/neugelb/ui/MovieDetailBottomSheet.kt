package com.example.neugelb.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.neugelb.R
import com.example.neugelb.compose.component.LabelValueCell
import com.example.neugelb.compose.component.text.ContentText
import com.example.neugelb.compose.theme.FiftySixDp
import com.example.neugelb.compose.theme.TwentyFourDp
import com.example.neugelb.model.InfoAndCredits

const val DIRECTOR_TITLE: String = "Director"

@ExperimentalMaterialApi
@Composable
fun MovieInfoBottomSheet(
    info: InfoAndCredits?
) {
    Column {
        info?.let { info ->
            LabelValueCell(
                labelText = stringResource(id = R.string.overview),
                modifier = Modifier.padding(horizontal = TwentyFourDp),
                valueText = info.overview?.takeIf { it.isNotEmpty() }
                    ?: stringResource(id = R.string.no_overview),
                bottomDivider = true
            )

            info.tagline?.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    labelText = stringResource(id = R.string.tagline),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it,
                    bottomDivider = true
                )
            }

            info.genres.map { it.name }.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    labelText = stringResource(id = R.string.genres),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it.joinToString(", "),
                    bottomDivider = true
                )
            }

            val actors = info.credits.cast.takeIf { it.isNotEmpty() }
            actors?.subList(0, actors.size.coerceAtMost(5))
                ?.let {
                    LabelValueCell(
                        labelText = stringResource(id = R.string.cast),
                        modifier = Modifier.padding(horizontal = TwentyFourDp),
                        valueText = it.joinToString(", ") { it.name },
                        bottomDivider = true
                    )
                }

            val directors = info.credits.crew.filter { it.job == DIRECTOR_TITLE }
            directors.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    labelText = stringResource(id = R.string.directors),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it.joinToString(", ") { it.name },
                    bottomDivider = true
                )
            }

            LabelValueCell(
                labelText = stringResource(id = R.string.rating),
                modifier = Modifier.padding(horizontal = TwentyFourDp),
                valueText = info.popularity.toString(),
                bottomDivider = true
            )

            info.homepage?.takeIf { it.isNotEmpty() }?.let {
                LabelValueCell(
                    labelText = stringResource(id = R.string.homepage),
                    modifier = Modifier.padding(horizontal = TwentyFourDp),
                    valueText = it,
                    bottomDivider = true
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
