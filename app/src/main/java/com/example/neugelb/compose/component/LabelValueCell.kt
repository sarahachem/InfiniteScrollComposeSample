package com.example.neugelb.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.neugelb.compose.component.text.BodyText
import com.example.neugelb.compose.component.text.ContentText
import com.example.neugelb.compose.component.text.SecondaryText
import com.example.neugelb.compose.theme.DarkThemePreviewParamProvider
import com.example.neugelb.compose.theme.FourDp
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.SixteenDp

@Composable
fun LabelIconCell(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    textAlign: TextAlign? = null,
    text: String,
    color: Color = NeugelbTheme.colors.textSecondary
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(FourDp), verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            icon()
        }
        ContentText(modifier, textAlign = textAlign, text = text, color = color)
    }
}

@Composable
fun LabelValueCell(
    modifier: Modifier = Modifier,
    labelText: String,
    valueText: String? = null,
    bottomDivider: Boolean = false,
) {
    CellLayout(
        modifier = modifier,
        label = { BodyText(modifier = modifier, text = labelText, textAlign = TextAlign.Start) },
        value = { valueText?.let { SecondaryText(text = valueText, textAlign = TextAlign.Start) } },
        bottomDivider = bottomDivider,
    )
}


@Composable
private fun CellLayout(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    label: @Composable () -> Unit,
    value: (@Composable () -> Unit)? = null,
    bottomDivider: Boolean = false,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .then(modifier)
    ) {
        Content(
            label = label,
            value = value,
        )
        if (bottomDivider) Divider(color = NeugelbTheme.colors.divider)
    }
}

@Composable
private fun Content(
    label: @Composable () -> Unit,
    value: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = SixteenDp)
    ) {
        ConstraintLayout(constraintSet = constraints(), modifier = Modifier.weight(1f)) {
            Box(Modifier.layoutId("label")) {
                label()
            }
            value?.let {
                Box(
                    modifier = Modifier.layoutId("value"),
                    contentAlignment = Alignment.CenterStart
                ) {
                    it()
                }
            }
        }
    }
}

fun constraints(): ConstraintSet {
    return ConstraintSet {
        val label = createRefFor("label")
        val value = createRefFor("value")
        val guidelineEnd = createGuidelineFromStart(0.5f)
        constrain(label) {
            linkTo(start = parent.start, end = value.start, bias = 0f)
            centerVerticallyTo(parent)
            width = Dimension.fillToConstraints
        }
        constrain(value) {
            linkTo(start = guidelineEnd, end = parent.end, bias = 0f)
            centerVerticallyTo(parent)
            width = Dimension.preferredWrapContent
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "SampleInputField", showBackground = true)
@Composable
fun SampleLabelValueCell(@PreviewParameter(DarkThemePreviewParamProvider::class) isDarkTheme: Boolean) {
    NeugelbTheme(darkTheme = isDarkTheme) {
        Column {
            LabelValueCell(
                labelText = "I'm the first label",
                valueText = "I'm the first text",
                bottomDivider = true
            )
            LabelValueCell(
                labelText = "I'm the second label",
                valueText = "I'm the second text",
                bottomDivider = true
            )
        }
    }
}
