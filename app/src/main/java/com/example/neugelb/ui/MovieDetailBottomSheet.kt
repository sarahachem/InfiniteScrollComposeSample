package com.example.neugelb.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.neugelb.R
import com.example.neugelb.compose.component.LabelValueCell
import com.example.neugelb.compose.theme.EightDp
import com.example.neugelb.compose.theme.TwentyFourDp
import com.example.neugelb.model.InfoAndCredits

@ExperimentalMaterialApi
@Composable
fun MovieInfoBottomSheet(
    info: InfoAndCredits?
) {
    Column {
        LabelValueCell(
            labelText = stringResource(id = R.string.overview),
            modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
            valueText = info?.overview?.takeIf { it.isNotEmpty() }
                ?: "Oops this movie has no overview",
            bottomDivider = true
        )

        info?.tagline?.takeIf { it.isNotEmpty() }?.let {
            LabelValueCell(
                labelText = stringResource(id = R.string.tagline),
                modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
                valueText = it,
                bottomDivider = true
            )
        }

        info?.genres?.map { it.name }?.takeIf { it.isNotEmpty() }?.let {
            LabelValueCell(
                labelText = stringResource(id = R.string.genres),
                modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
                valueText = it.joinToString(", "),
                bottomDivider = true
            )
        }

        val actors = info?.credits?.cast?.filter { it.known_for_department == "Acting" }

        actors?.takeIf { it.isNotEmpty() }?.subList(0, if (actors.size > 5) 5 else actors.size)
            ?.let {
                LabelValueCell(
                    labelText = stringResource(id = R.string.cast),
                    modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
                    valueText = it
                        .joinToString(", ") { it.name },
                    bottomDivider = true
                )
            }

        val directors = info?.credits?.cast?.filter { it.known_for_department == "Directing" }
            ?.distinctBy { it.original_name }?.map { it.original_name }?.takeIf { it.isNotEmpty() }
            ?: info?.credits?.crew?.filter { it.known_for_department == "Directing" }
                ?.distinctBy { it.original_name }
                ?.map { it.original_name }

        directors?.takeIf { it.isNotEmpty() }
            ?.subList(0, if (directors.size > 3) 2 else directors.size)?.let {
                LabelValueCell(
                    labelText = stringResource(id = R.string.directors),
                    modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
                    valueText = it.joinToString(", "),
                    bottomDivider = true
                )
            }

        LabelValueCell(
            labelText = stringResource(id = R.string.rating),
            modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
            valueText = info?.popularity.toString(),
            bottomDivider = true
        )

        info?.homepage?.takeIf { it.isNotEmpty() }?.let {
            LabelValueCell(
                labelText = stringResource(id = R.string.homepage),
                modifier = Modifier.padding(horizontal = TwentyFourDp, vertical = EightDp),
                valueText = it,
                bottomDivider = true
            )
        }
    }
}
