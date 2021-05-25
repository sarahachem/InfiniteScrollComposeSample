package com.example.neugelb.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp

private val LightColorPalette = ColorPalette(
    textPrimary = ThemeColorAlias.textMain.light,
    textSecondary = ThemeColorAlias.textSecondary.light,
    textPlaceholder = ThemeColorAlias.textPlaceholder.light,
    divider = ThemeColorAlias.divider.light,
    actionPrimary = ThemeColorAlias.actionMain.light,
    onActionPrimary = ThemeColorAlias.onClickMain.light,
    actionPrimaryDisabled = ThemeColorAlias.actionMainDisabled.light,
    iconMain = ThemeColorAlias.iconMain.light,
    playerBackground = ThemeColorAlias.playerBackground.light
)

private val DarkColorPalette = ColorPalette(
    textPrimary = ThemeColorAlias.textMain.dark,
    textSecondary = ThemeColorAlias.textSecondary.dark,
    textPlaceholder = ThemeColorAlias.textPlaceholder.dark,
    divider = ThemeColorAlias.divider.dark,
    actionPrimary = ThemeColorAlias.actionMain.dark,
    onActionPrimary = ThemeColorAlias.onClickMain.dark,
    actionPrimaryDisabled = ThemeColorAlias.actionMainDisabled.dark,
    iconMain = ThemeColorAlias.iconMain.dark,
    playerBackground = ThemeColorAlias.playerBackground.dark
    )

private val MaterialLightPalette = lightColors(
    primary = ThemeColorAlias.backgroundMain.light,
    surface = ThemeColorAlias.backgroundSecondary.light,
    onSurface = ThemeColorAlias.textMain.light,
    error = ThemeColorAlias.actionMainError.light,
    onError = ThemeColorAlias.onClickMain.light,
    background = ThemeColorAlias.backgroundMain.light
)

private val MaterialDarkPalette = darkColors(
    primary = ThemeColorAlias.backgroundMain.dark,
    surface = ThemeColorAlias.backgroundSecondary.dark,
    onSurface = ThemeColorAlias.textMain.dark,
    error = ThemeColorAlias.actionMainError.dark,
    onError = ThemeColorAlias.onClickMain.dark,
    background = ThemeColorAlias.backgroundMain.dark
)

private val Shapes = Shapes(
    small = RoundedCornerShape(2.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun NeugelbTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    val materialColors = if (darkTheme) MaterialDarkPalette else MaterialLightPalette
    val typeList = typeList()
    NeugelbColors(colors, typeList) {
        MaterialTheme(
            colors = materialColors,
            shapes = Shapes,
            content = content
        )
    }
}

object NeugelbTheme {

    val colors: ColorPalette
        @Composable get() = LocalColor.current

    val types: List
        @Composable get() = LocalType.current
}

@Stable
class List(
    body1: TextStyle,
    body2: TextStyle,
    data3: TextStyle,
    secondary: TextStyle,
    header1: TextStyle,
    header3: TextStyle,
    button1: TextStyle
) {
    var body1 by mutableStateOf(body1)
        private set
    var body2 by mutableStateOf(body2)
        private set
    var data3 by mutableStateOf(data3)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var header1 by mutableStateOf(header1)
        private set
    var header3 by mutableStateOf(header3)
        private set
    var button1 by mutableStateOf(button1)
        private set

    fun update(other: List) {
        body1 = other.body1
        body2 = other.body2
        data3 = other.data3
        secondary = other.secondary
        header1 = other.header1
        header3 = other.header3
        button1 = other.button1
    }
}

@Stable
class ColorPalette(
    textPrimary: Color,
    textSecondary: Color,
    textPlaceholder: Color,
    divider: Color,
    actionPrimary: Color,
    onActionPrimary: Color,
    actionPrimaryDisabled: Color,
    iconMain: Color,
    playerBackground: Color
) {
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textPlaceholder by mutableStateOf(textPlaceholder)
        private set
    var divider by mutableStateOf(divider)
        private set
    var mainColor by mutableStateOf(actionPrimary)
        private set
    var onClick by mutableStateOf(onActionPrimary)
        private set
    var mainColorDisabled by mutableStateOf(actionPrimaryDisabled)
        private set
    var iconMain by mutableStateOf(iconMain)
        private set
    var playerBackground by mutableStateOf(playerBackground)
        private set

    fun update(other: ColorPalette) {
        textPrimary = other.textPrimary
        textPlaceholder = other.textPlaceholder
        divider = other.divider
        mainColor = other.mainColor
        onClick = other.onClick
        mainColorDisabled = other.mainColorDisabled
        iconMain = other.iconMain
        playerBackground = other.playerBackground
    }
}

@Composable
fun NeugelbColors(
    colors: ColorPalette,
    types: List,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    val typeList = remember { types }
    typeList.update(types)
    CompositionLocalProvider(
        LocalColor provides colorPalette,
        LocalType provides typeList,
        content = content
    )
}

private val LocalType = staticCompositionLocalOf<List> {
    error("No PrimerTypePalette provided")
}
private val LocalColor = staticCompositionLocalOf<ColorPalette> {
    error("No PrimerColorPalette provided")
}

class DarkThemePreviewParamProvider : CollectionPreviewParameterProvider<Boolean>(
    listOf(false, true)
)
