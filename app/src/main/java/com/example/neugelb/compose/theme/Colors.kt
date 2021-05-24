package com.example.neugelb.compose.theme

import androidx.compose.ui.graphics.Color

data class ThemedColor(val light: Color, val dark: Color)
fun ThemedColor.forTheme(isDark: Boolean) = if (isDark) dark else light

internal object ThemeColorAlias {
    val backgroundMain = ThemedColor(light = ColorAlias.lightGrey, dark = ColorAlias.black)
    val backgroundSecondary = ThemedColor(light = ColorAlias.disabledGrey, dark = ColorAlias.darkGrey)
    val textMain = ThemedColor(light = ColorAlias.black, dark = ColorAlias.lightGrey)
    val textSecondary = ThemedColor(light = ColorAlias.black, dark = ColorAlias.veryLightGrey)
    val textPlaceholder = ThemedColor(light = ColorAlias.grey, dark = ColorAlias.disabledGrey)
    val actionMain = ThemedColor(light = ColorAlias.blue, dark = ColorAlias.blue)
    val actionMainError = ThemedColor(light = ColorAlias.red, dark = ColorAlias.red)
    val actionMainDisabled = ThemedColor(light = ColorAlias.disabledGrey, dark = ColorAlias.disabledGrey)
    val onClickMain = ThemedColor(light = ColorAlias.lightGrey, dark = ColorAlias.lightGrey)
    val divider = ThemedColor(light = ColorAlias.veryLightGrey, dark = ColorAlias.grey)
    val theme = ThemedColor(light = ColorAlias.cyan, dark = ColorAlias.cyan)
    val iconMain = ThemedColor(light = ColorAlias.lightblue, dark = ColorAlias.lightblue)
}

private object ColorAlias {
    val black = Color(0xff000000)
    val darkGrey = Color(0xff23212C)
    val grey = Color(0xff3F424B)
    val disabledGrey = Color(0xffD8DAE1)
    val veryLightGrey = Color(0xffE5E6EA)
    val lightGrey = Color(0xffFFFFFF)
    val blue = Color(0xff0009A9)
    val red = Color(0xffD93421)
    val cyan = Color(0xff5CB2CC)
    val lightblue = Color(0XFF00B3E5)
}
    